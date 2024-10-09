# SpotifyRemastered

This is a client-server socket application based on the popular streaming service Spotify.
The project follows the requirements from Modern Java Technologies Course at FMI, Sofia University, which can be found [here](https://github.com/fmi/java-course/blob/mjt-2023-2024/course-projects/spotify.md).

The application enables communication between a server and multiple clients over sockets using a command line interface (CLI). The primary objective is to provide music streaming services to users.

# Requirements
- Java 21 or later
- Mockito
The project utilizes `java.nio`, `java.net` and `javax.sound` standard libraries, and Mockito for unit testing.   

# How to run
1. Clone the repository.
2. Open the project using an IDE like Intellij IDEA.
3. Run `SpotifyServer.java`
4. Run `SpotifyClient.java` or create more instances of it.

The client's default server host is `localhost`. To change that - simply change the respective field in `SpotifyClient.java`.

## Commands
|Command|Function|Example Request|
|:-:|:-:|:-:|
| `register` \<email> \<password> | Registers a new user. | "register example@example pass123" |
| `login` \<email> \<password> | Logs in the user. | "login example@example pass123" |
| `unregister`  | Unregisters the currently-logged in user. | "unregister" |
| `logout` or `disconnect` | Logs out the user. | "logout" or "disconnect" |
| `search` \<words> | Searches for songs by one or more words included in the title or the artist's name, and print the most relevant results. | "search AC DC"  |
| `top` \<number> | Returns the most played songs at the moment. | "top 10" |
| `create-playlist` \<name_of_the_playlist> | Create an empty playlist. | "create-playlist my_favorites" |
| `add-song-to` \<name_of_the_playlist> \<artist-song> | Adds a song to a playlist. If the playlist does not exist, it is automatically created. | "add-song-to my_favorites Queen - Bohemian Rapsody" |
| `show-playlist` \<name_of_the_playlist> | Shows the songs from a given playlist. | "show-playlist example_playlist" |
| `play` \<words> | Plays a song, if it exists in the database. | "play The Cranberries - Zombie" |
| `stop` | Stops a song. | "stop" |  

The server will send the appropriate response if a command was successful or not.

# Server-side Implementation
- Client data are stored in `.txt` files, such as emails, passwords and playlists. For this project it does not matter much, but in the future, hashing or encrypting will be essential for improved privacy and security.
- The song database are stored in their own separate folder. The names of the songs must follow the structure `artist - song_name`. Only the `.wav` format is supported.
- The user data and songs data are cached at run-time for better performance. A new user's data is automatically adjusted when they register or unregister, and the operation is thread-safe.
- There is no limit to the amount of users that can connect.

# Client-side Implementation
- The user can enter the commands
- Music streaming occurs on a separate thread, allowing you to contiunue sending new commands.
- If something crashes or goes wrong, a logger will output a `.txt` file with the error log and additional details.

# Credits
Special thanks to Stoyan Velev and his team for their exceptional course! Be sure to check it out!
