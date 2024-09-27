package spotify.server;

import spotify.logger.Logger;
import spotify.server.command.executor.CommandExecutor;
import spotify.server.command.factory.CommandFactory;
import spotify.server.user.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SpotifyServer {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 1337;
    private static final int BUFFER_SIZE = 1024;

    private static boolean online = true;

    private final SpotifyManager spotifyManager;
    private final Map<SocketChannel, Client> clients;

    public SpotifyServer() {
        this.clients = new HashMap<>();
        this.spotifyManager = new SpotifyManager();
    }

    public static void main(String[] args) {
        SpotifyServer server = new SpotifyServer();
        server.startServer();
    }

    public void startServer() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            System.out.println("Server listening on port " + SERVER_PORT);
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (online) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        handleAcceptableKey(channel, selector);
                    } else if (key.isReadable()) {
                        handleReadableKey(key, buffer);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAcceptableKey(ServerSocketChannel channel, Selector selector) throws IOException {
        SocketChannel clientChannel = channel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);

            Client client = new Client(clientChannel, User.of("Guest", "Guest"));
            clients.put(clientChannel, client);
            System.out.println("A client has connected: " + clients.get(client.getSocketChannel()).getEmail());
        }
    }

    private void handleReadableKey(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        buffer.clear();

        try {
            int r = clientChannel.read(buffer);
            if (r < 0) {
                System.out.println("Client has closed the connection");
                clientChannel.close();
                clients.remove(clientChannel);
                return;
            }

            buffer.flip();

            Client clientHandler = clients.get(clientChannel);
            handleClient(clientChannel, buffer, clientHandler);

        } catch (SocketException e) {
            System.out.println("Connection reset by client: " + e.getMessage());
            clientChannel.close();
            if (spotifyManager.getUserManager().isUserLoggedIn(clients.get(clientChannel).getEmail())) {
                spotifyManager.getUserManager().logoutUser(clients.get(clientChannel).getEmail());
            }
            clients.remove(clientChannel);
        }
    }

    private void handleClient(SocketChannel clientChannel, ByteBuffer buffer, Client client) throws IOException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String clientData = new String(bytes, StandardCharsets.UTF_8);

        System.out.println("Received data from " + client.getEmail() + ": " + clientData);

        if (clientData.equals("shutdown") && client.getEmail().equals("admin@admin.com")) {
            System.out.println("Shutting down server...");
            online = false;
            clientChannel.close();
            clients.remove(clientChannel);
        }

        String processedCommand = processClientMessage(client, clientData);
        if (processedCommand == null) {
            throw new RuntimeException("Unexpected command error.");
        }
        clientChannel.write(ByteBuffer.wrap(processedCommand.getBytes(StandardCharsets.UTF_8)));
    }

    private String processClientMessage(Client clientHandler, String clientMessage) {
        CommandExecutor cmdExec = new CommandExecutor();
        String reply;
        try {
            reply = cmdExec.execute(CommandFactory.create(clientMessage, clientHandler, spotifyManager));
        } catch (Exception e) {
            reply = e.getMessage();
            Logger.logError(clientHandler.getEmail(), e);
        }
        return reply;
    }
}
