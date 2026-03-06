## Comment-to-Ticket Triage

This SpringBoot application automatically checks messages coming in via post request
to determine whether it is a complaint. Complaints are identified using llm, which
analyzes the message and ultimately assigns a priority to the complaint (the higher the priority, the more urgent it is)
and a type (bug, feature, billing, account, other).

## How it works
1) The user sends a message via the UI or REST API.
2) The AI model analyzes the comment.
3) If the message is determined to be a complaint, a ticket is created
   (with title, category, priority, and summary).
4) Tickets (as well as any messages) are stored in the H2 database.

## Tech Stack
- Java 17, Spring Boot 3.2
- Spring AI (OpenAI-compatible client to HuggingFace)
- H2 database + Flyway
- Thymeleaf
- MapStruct, Lombok

## Prerequisites
- Java 17+
- Maven 3.8+
- HuggingFace account and API token → https://huggingface.co/settings/tokens


## Setup & Run

**1. Clone the repository**
git clone git@github.com:Yahhweh/Comment-to-Ticket-Triage.git 
or
git clone https://github.com/Yahhweh/Comment-to-Ticket-Triage.git

cd comment-to-ticket-triage

**2. Create secrets.yml in the project root**
huggingface:
token: YOUR_TOKEN_HERE

**3. Run**
./mvnw spring-boot:run

App will start on http://localhost:8080

## Database
H2 console available at: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:pulsedesk
Username: user
Password: 1234


## API
- POST /comments (submit a new comment)
- GET /comments (get all comments)
- GET /tickets (get all tickets)
- GET /tickets/{ticketId} (get ticket by id)

## UI
- /ui/comment (submit a comment)
- /ui/comment/all (view all comments)
- /ui/ticket/all (view all tickets)




