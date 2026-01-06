## DevOps & CI/CD Workflow

This project implements a robust, modern DevOps pipeline that automates software delivery following several key best practices:

### Branching Strategy

All development work is done on feature branches, which are then merged into the `dev` branch after review and testing.  

Once features are considered stable and ready for release, changes from `dev` are merged into `main`, which is the protected branch and represents production-ready code.

### CI/CD Pipeline Overview

The delivery process is fully automated using GitHub Actions.  
Key pipeline steps include:

1. **Open Issue & Feature Branch Creation:**  
   - Development starts by opening an issue and creating a feature branch from `dev`.
2. **Continuous Integration on PRs and Pushes to main/dev:**  
   - Triggered by push or pull request on `main` or `dev` branches.
   - **Build & Unit Test:** Automatically compiles code and runs Java unit tests (JUnit via Maven).
   - **Linter & Style Check:** Runs Checkstyle and SpotBugs for static code analysis and style enforcement.
   - **SAST (Static Application Security Testing):** Uses Snyk for security scanning with enforcement (pipeline fails on severe issues).
   - **Build Docker Image & Scan:**  
     - Builds Docker image.
     - Runs Trivy for Docker vulnerability scanning.
3. **Continuous Delivery (CD) to Kubernetes (K8s):**  
   - Triggered after a successful CI run on `dev`.
   - **Docker Push:** Pushes the Docker image to Docker Hub.
   - **Kubernetes Deployment:**  
     - Uses Minikube for testing.
     - Deploys the built image using Kubernetes manifests from the `/k8s` directory.
     - Monitors rollout and enables rollback on deployment failure.

### Future Improvements
- Add a relational database (e.g., PostgreSQL) for user profiles, playlists, or playback history.
- Store uploaded songs in AWS S3 for scalable, cloud-based storage.
- Deploy a test environment on AWS EC2 for integration and end-to-end test automation.