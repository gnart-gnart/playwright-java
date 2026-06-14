# Java Playwright Hybrid API/UI Automation Framework
A minimalist, high-performance automated testing pipeline built with **Java 17**, **Playwright**, and **JUnit 5**. This framework demonstrates a "Shift-Left" testing methodology by verifying backend infrastructure states via a live REST API layer before spinning up isolated browser contexts to audit local web assets.

## Architecture & Design Highlights
*   **Zero Local Server Overhead:** Leverages Playwright's capability to mount local filesystem resources securely via the `file://` protocol, bypassing the need for Docker containers, Apache, or Node-based servers for lightweight UI testing.
*   **Native API Request Contexts:** Utilizes Playwright's built-in `APIRequestContext` rather than heavy external libraries like RestAssured, keeping the framework dependency tree clean and execution speeds blazing fast.
*   **Dynamic UI Validation:** Targets responsive frontend interactions on a custom HTML component using strict modern CSS and locator patterns.

---

## Tech Stack
*   **Language:** Java 17
*   **Test Runner:** JUnit 5 (Jupiter Engine)
*   **Automation Core:** Microsoft Playwright Java SDK
*   **Build Tool:** Maven 3.x

---

## Getting Started
### Prerequisites (macOS Setup)
Ensure you have Java 17 and Maven installed on your machine. If you use Homebrew, run:
```bash
brew install openjdk@17
brew install maven