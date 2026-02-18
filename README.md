# MyPortfolios GEI API

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/spring-template?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/12)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/spring-template/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/spring-template/actions)
[![CucumberReports: UdL-EPS-SoftArch](https://messages.cucumber.io/api/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0/badge)](https://reports.cucumber.io/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0)
[![Deployment status](https://img.shields.io/uptimerobot/status/m792691238-18db2a43adf8d8ded474f885)](https://spring-template.fly.dev/users)

## Vision

**For** ... **who** want to ...
**the project** ... **is an** ...
**that** allows ...
**Unlike** other ...

## Features per Stakeholder

| CREATOR                                   | ADMIN                | ANONYMOUS                    |
|-------------------------------------------|----------------------|------------------------------|
| Register                                  | Add Admin            | View public creator profiles |
| Login                                     | Login                | List public portfolios       |
| Logout                                    | Logout               | List portfolio projects      |
| Edit profile                              | Suspend Creator      | List project content         |
| Create portfolio                          | List flagged content | Search public content        |
| Edit portfolio                            | Remove content       | Report public content        |
| Create project                            |                      |                              |
| Edit project                              |                      |                              |
| Add content                               |                      |                              |
| Edit content                              |                      |                              |
| Create tag                                |                      |                              |
| Tag content                               |                      |                              |
| Set public / private / restricted         |                      |                              |
| Share restricted with user                |                      |                              |
| List public and shared portfolios         |                      |                              |
| List public and shared portfolio projects |                      |                              |
| List public and shared project content    |                      |                              |
| Search public and shared content          |                      |                              |
| Report content                            |                      |                              |

## Entities Model

```mermaid
classDiagram
%% Interface Definition
    class UserDetails {
        <<interface>>
    }

%% Base Entity
    class UriEntity {
        uri : String
    }

%% User Hierarchy
    class User {
        username : String
        password : String
        email : String
    }

    class Admin {
    %% Placeholder fields 'i' in image omitted
    }

    class Creator {
    %% Placeholder fields 'i' in image omitted
    }

%% Core Domain Entities
    class Portfolio {
        name: String
        description: String
        created: ZonedDateTime
        visibility: Enum
        modified: ZonedDateTime
    }

    class Project {
        name: String
        description: String
        created: ZonedDateTime
        visibility: Enum
        modified: ZonedDateTime
    }

    class Content {
        name: String
        description: String
        created: ZonedDateTime
        visibility: Enum
        modified: ZonedDateTime
    }

    class Tag {
        name: String
        description: String
        created: ZonedDateTime
        modified: ZonedDateTime
    }

    class Report {
        user: String
        reason: String
        created: ZonedDateTime
    }

%% Inheritance Relationships (Hollow Triangles)
    UriEntity <|-- User
    UriEntity <|-- Portfolio
    UriEntity <|-- Project
    UriEntity <|-- Content
    UriEntity <|-- Tag

    UserDetails <|.. User : implements
    User <|-- Admin
    User <|-- Creator

%% Associations and Multiplicities

%% Portfolio is owned by 1 User
    User "1" <-- "*" Portfolio : ownedBy

%% Portfolio contains many Projects
    Portfolio "1" <-- "*" Project

%% Project contains many Content items
    Project "1" <-- "*" Content

%% Content has many Tags (Many-to-Many)
    Content "*" <--> "*" Tag

%% Report refers to 1 Content
    Content "1" <-- "*" Report

%% Creator has access to many Content items
    Creator --> "*" Content : has acces to
```
