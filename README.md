<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/baqterya/muzukanji">
    <img src="images/muzukanji-high-resolution-logo-banner.png" alt="Logo">
  </a>
</div>





<!-- ABOUT THE PROJECT -->
## About The Project
[![MIT License][license-shield]][license-url]
<br/>
A Spring Boot API that stores 13108 Kanji characters. The records contain each kanji's meanings,
possible readings in both kana and romaji, the number of strokes, JLPT level, Jyoyo Grade and
newspaper frequency if applicable. It uses PostgreSQL as a database and Keycloak to secure the protected endpoints.
<br />
<a href="" placeholder="Work in progress"><strong>Explore the docs »</strong></a>

<br />
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#prerequisites">Open endpoints</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
   </li>
    <li><a href="#license">License</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

### Built With

* [![Java][Java-badge]][Java-url]
* [![SpringBoot][SpringBoot-badge]][SpringBoot-url]
* [![PostgreSQL][Postgres-badge]][Postgres-url]
* [![Keycloak][Keycloak-badge]][Keycloak-url]
* [![Docker][Docker-badge]][Docker-url]
* [![TestContainers][TestContainers-badge]][TestContainers-url]

  
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

<strong> !The API is not yet hosted! </strong>

### Prerequisites


* [Java 17 or higher](https://www.oracle.com/java/technologies/downloads/)
* [Docker](https://www.docker.com/)

### Local Setup

1. Navigate to the root directory.
2. Run maven clean package:
    ```sh
    mvn clean package
   ```
3. Run the docker-compose.yaml
   ```sh
   docker compose up
   ```
4. If all images in the container are running you'll be able to access the API at the port 5555
   ```sh
   curl http://localhost:5555/api/v1/kanji
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

### OPEN ENDPOINTS

1. Paginated list of all kanji
   ```sh
   GET /api/v1/kanji
   ```
   The returned JSON contains a ``pagination`` header containing the metadata and links to
   other pages.
   The ``data`` segment contains the list of all kanji containing the kanji itself, it's english meanings
   and a link to its details. It can be filtered with  following optional **Request Params**:

   | Param         |  Type   |                  Constraints                  |
   |:--------------|:-------:|:---------------------------------------------:|
   | kanji         | string  |          Must be japanese characters          |
   | meaning       | string  |                No constraints                 |
   | kunyomi       | string  |          Must be japanese characters          |
   | kunyomiRomaji | string  |           Must be roman characters            |
   | onyomi        | string  |          Must be japanese characters          |
   | onyomiRomaji  | string  |           Must be roman characters            |
   | minStrokes    | integer |               Between 1 and 34                |
   | maxStrokes    | integer |               Between 1 and 34                |
   | minJlptLevel  | string  | One of: N1, N2, N3, N4, N5 (can be lowercase) |
   | maxJlptLevel  | string  | One of: N1, N2, N3, N4, N5 (can be lowercase) |
   | minJyoyoGrade | integer |               Between 1 and 10                |
   | maxJyoyoGrade | integer |               Between 1 and 10                |
   | minUsage      | integer |              Between 1 and 2051               |
   | maxUsage      | integer |              Between 1 and 2051               |

2. Kanji details, accessed by ID
   ```sh
   GET /api/v1/kanji/1
   ```
   It will return the output JSON like:
   ```json
   {
      "id": 1,
      "kanji": "一",
      "meanings": "One, One Radical (no.1)",
      "kunyomi": "ひと-, ひと.つ",
      "kunyomiRomaji": "hito-, hito.tsu",
      "onyomi": "イチ, イツ",
      "onyomiRomaji": "ichi, itsu",
      "strokes": 1,
      "jlptLevel": "N5",
      "jyoyoGradeTaught": 1,
      "mostUsedInNewspapers": 2
   }  
   ```
   
### PROTECTED ENDPOINTS

   To access the protected endpoints you need a bearer token issued from the Keycloak API.
   These endpoints are meant to be used to maintain the database and not be accessible to the
   client user. 

1. Create Kanji
   ```sh
   POST /api/v1/kanji
   ```
   Body must contain a properly formatted JSON with the Kanji's data. ID assignment is handled by
   the database. The Kanji field cannot be null.
   ```json
   {
      "kanji": "一",
      "meanings": "One, One Radical (no.1)",
      "kunyomi": "ひと-, ひと.つ",
      "kunyomiRomaji": "hito-, hito.tsu",
      "onyomi": "イチ, イツ",
      "onyomiRomaji": "ichi, itsu",
      "strokes": 1,
      "jlptLevel": "N5",
      "jyoyoGradeTaught": 1,
      "mostUsedInNewspapers": 2
   }  
   ```
2. Update Kanji
   ```sh
   PUT /api/v1/kanji/1
   ```
   It updates the kanji under the chosen ID with the data included in the JSON in the request body.
   The ID must be an integer not smaller than 1. There must exist a kanji under this ID.
   ```json
   {
      "kanji": "一",
      "meanings": "One, One Radical (no.1)",
      "kunyomi": "ひと-, ひと.つ",
      "kunyomiRomaji": "hito-, hito.tsu",
      "onyomi": "イチ, イツ",
      "onyomiRomaji": "ichi, itsu",
      "strokes": 1,
      "jlptLevel": "N5",
      "jyoyoGradeTaught": 1,
      "mostUsedInNewspapers": 2
   } 
   ```
3. Delete Kanji
   ```sh
   DELETE /api/v1/kanji/1
   ```
   It removes the Kanji under chosen ID from the database.
   

[//]: # (_For more examples, please refer to the [Documentation]&#40;https://example.com&#41;_)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [Paul O'Leary McCann for cutlet - a python japanese to romaji converter](https://github.com/polm/cutlet)
* [David Gouveia for creating a Kanji JSON dataset I used to initalise the database](https://github.com/davidluzgouveia/kanji-data)
* [Othneil Drew for creating a great readme template](https://github.com/othneildrew/Best-README-Template)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/baqterya/muzukanji/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo_name.svg?style=for-the-badge
[forks-url]: https://github.com/baqterya/muzukanji/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo_name.svg?style=for-the-badge
[stars-url]: https://github.com/baqterya/muzukanji/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo_name.svg?style=for-the-badge
[issues-url]: https://github.com/baqterya/muzukanji/issues
[license-shield]: https://img.shields.io/badge/License-MIT-yellow.svg
[license-url]: https://github.com/baqterya/muzukanji/blob/main/LICENSE
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/linkedin_username
[product-screenshot]: images/screenshot.png

[Java-url]: https://www.java.com
[Java-badge]: https://img.shields.io/badge/java-ff9f00?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4NCjwhLS0gVXBsb2FkZWQgdG86IFNWRyBSZXBvLCB3d3cuc3ZncmVwby5jb20sIFRyYW5zZm9ybWVkIGJ5OiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4KPHN2ZyBmaWxsPSIjZmZmZmZmIiB2ZXJzaW9uPSIxLjEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiIHdpZHRoPSI4MDBweCIgaGVpZ2h0PSI4MDBweCIgdmlld0JveD0iMCAwIDUxMiA1MTIiIGVuYWJsZS1iYWNrZ3JvdW5kPSJuZXcgMCAwIDUxMiA1MTIiIHhtbDpzcGFjZT0icHJlc2VydmUiPgoNPGcgaWQ9IlNWR1JlcG9fYmdDYXJyaWVyIiBzdHJva2Utd2lkdGg9IjAiLz4KDTxnIGlkPSJTVkdSZXBvX3RyYWNlckNhcnJpZXIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgoNPGcgaWQ9IlNWR1JlcG9faWNvbkNhcnJpZXIiPiA8ZyBpZD0iNTE1MWUwYzg0OTJlNTEwM2MwOTZhZjg4YTUxZThkODEiPiA8cGF0aCBkaXNwbGF5PSJpbmxpbmUiIGQ9Ik0xOTMuOTE4LDIwOC4zNjljLTQuNzI5LTEwLjQ1Ni02Ljg0OS0yMi42NTItMy4yMzYtMzMuNzMxYzMuNjEyLTExLjMyNywxMS43MDMtMjAuNDEzLDE5Ljc5NC0yOC44NzggYzIyLjUyNS0yMi41MzEsNTAuMjg1LTM5LjA4NSw3Mi4zMTYtNjEuOTg2YzEyLjE5Ny0xMi41NzMsMjIuMjc4LTI3LjYzNCwyNS43NjItNDQuOTM3YzIuODY0LTEyLjY5NSwxLjQ5Ni0yNS43NjQtMS4xMTctMzguMzM3IGMxMS43LDEzLjMxOSwxNS41NTksMzIuMzYzLDEyLjE5Nyw0OS41NDFjLTMuNjA4LDE5LjI5Mi0xNC4zMTYsMzYuMzQ0LTI2Ljg4Niw1MS4wMzFjLTEwLjA4MSwxMS44MjctMjEuNjU5LDIyLjI4Mi0zMy43MzEsMzEuOTkzIGMtMTQuMDY1LDExLjMyNy0yNy44OCwyMy41MjQtMzYuNzE2LDM5LjQ1N2MtNy40NzIsMTIuOTQzLTkuMjE1LDI4Ljg3Ni00LjExLDQyLjk0MmM4LjM0MSwyNC4xNDYsMjcuNzU2LDQyLjA3MSwzOC4zMzgsNjQuODQ4IGMtMTEuNzAzLTEwLjMzMi0yMy4xNTItMjEuMDM2LTMzLjg2LTMyLjM2MUMyMTEuNDcxLDIzNi4wMDEsMjAwLjg4OSwyMjMuMzA3LDE5My45MTgsMjA4LjM2OXogTTI1Ny4zOTgsMTg5LjQ0OCBjLTIuMTE1LDE5Ljc5Miw4LjIxMywzOC40NjIsMjAuNTM5LDUzLjE1MWM1Ljk3Miw2LjU5NiwxMS4wNzYsMTQuNjg3LDExLjMyMywyMy44OTljMC4yNTEsMTIuMzE4LTYuNzE2LDIzLjc3NC0xNS42ODQsMzEuODYxIGMyLjExOS0wLjI0NiwzLjYxMi0yLjExNSw1LjM1NS0zLjExYzEzLjQ0My04LjU4OSwyNi4zODUtMTkuNDE4LDMyLjk4Mi0zNC4yMjdjNC4zNTctMTAuMDgzLDMuMzYyLTIyLjAzNC0yLjM2Mi0zMS4zNzEgYy02LjcyNC0xMC45NTMtMTUuNTU5LTIwLjY2Mi0yMC43ODYtMzIuNjFjLTIuODY3LTYuNzIxLTMuODYyLTE0LjU2Mi0xLjQ5Ni0yMS42NTdjMy4xMTQtOS41ODMsOS44MzQtMTcuNDI2LDE2LjkzLTI0LjI3MiBjMTkuNTQtMTguNTQ0LDQzLjE4OS0zMS43NDMsNjUuODQ0LTQ2LjE3OWMtMjguNjI5LDguMjE0LTU2Ljg4MywxOS41NDItODEuMDMsMzcuMzQzIEMyNzMuNzAyLDE1My43MjcsMjU5LjUxNSwxNjkuNjU4LDI1Ny4zOTgsMTg5LjQ0OHogTTM5My40NDcsMjgzLjA1MmMxMy41NjgsMC43NDgsMjYuODgyLDEwLjcwNCwyOS4zNzQsMjQuMzk3IGMyLjM2NiwxMS44MjUtMy4zNTgsMjMuNTI0LTEwLjcwNSwzMi40ODVjLTEyLjA3NSwxNC40MzgtMjguMzgyLDI0Ljc3MS00NC44MDcsMzMuNjA5Yy0xLjYyMiwwLjk5MS0yLjk5LDIuMjM3LTQuMjM1LDMuNjA4IGMyMS42NTktNS40NzgsNDMuMzE0LTEzLjY4OSw2MC44NjctMjcuNzU2YzkuNzA1LTguMDkxLDE4LjI5NC0xOC43OTksMjAuMTYzLTMxLjYxOWMxLjc0My0xMS4wNzYtMi44Ni0yMi41MjgtMTEuMDc3LTI5Ljg3MSBjLTkuOTYtOS4wOS0yNC4wMjEtMTIuNDQ4LTM3LjIxOC0xMC43MDRjLTcuNTkzLDAuOTk1LTE1LjkzMSwyLjYxMy0yMS4xNTgsOC45NjFDMzgwLjg3NywyODQuOTIxLDM4Ni45NzEsMjgyLjQyOSwzOTMuNDQ3LDI4My4wNTIgeiBNMTIzLjIyLDMxOC42NDdjMTYuMzAzLDQuMzU3LDMzLjEwOCw1LjYwMyw0OS43ODcsNi43MjRjMTQuOTM2LDAuOTk1LDI5Ljg3NSwxLjI0Niw0NC45MzcsMS4xMiBjMzguODMzLTAuNjE5LDc3LjkxNi0zLjIzNiwxMTYuMDAzLTExLjY5OWMzLjYwOC0wLjg3LDcuNTkzLTEuNDkzLDEwLjgzMy0zLjczM2M2LjM0Ny00LjExLDEzLjMxMy03LjM0NywyMC4xNjItMTAuNTgzIGMtMzAuOTk1LDQuOTc5LTYyLjExMyw5LjIxNS05My40NzgsMTEuMjA1Yy0zMS43NCwxLjk5MS02My43MzEsMy4yMzYtOTUuNTkzLDEuMTIxYy05LjA4Ni0wLjg3LTE4LjQyMy0xLjM3MS0yNi44ODYtNC44NTggYy0xLjk5NC0wLjg3LTQuNzMzLTIuNjA5LTMuNzM4LTUuMjI3YzEuODY5LTMuMzYxLDUuNjAzLTQuOTc3LDguODM5LTYuNzJjMTMuNjk0LTYuNDczLDI4LjYyOS0xMC4wODEsNDMuMTkzLTE0LjMxMyBjLTI1LjAyMS0wLjM3Ni00OS45MTYsNS45NzEtNzIuODE0LDE1LjgwNmMtNS4xMDUsMi40OTEtMTAuODMsNC40ODEtMTQuOTM2LDguNzE0Yy0xLjYyMiwxLjczOS0xLjYyMiw0LjczMiwwLjI0Nyw2LjIyMiBDMTEzLjUxMSwzMTUuNzg3LDExOC40ODcsMzE3LjI4LDEyMy4yMiwzMTguNjQ3eiBNMzI0Ljg2NCwzNTIuODhjLTIxLjc4NCwzLjg1OS00My42OTQsNy40NzItNjUuNzI2LDguNTg5IGMtMjQuMTQ3LDEuNjE4LTQ4LjI5NCwwLjI0Ny03Mi4xOTEtMi4yNDFjLTQuNjA0LTAuNjIzLTkuMjExLTEuMzY4LTEzLjMxNy0zLjQ4M2MtMi4xMTYtMS4yNDYtNC4yMzEtMy4yMzYtNC4xMDYtNS44NTQgYzAuMjQ3LTQuMTA2LDMuNzMtNi45NjcsNi4yMjItOS45NTZjLTcuNzE1LDIuNzM5LTE1LjQzNCw1LjU5OS0yMS45MDYsMTAuNzA4Yy0yLjc0MiwyLjExNi01LjQ3OCw1LjQ3NC00LjczMyw5LjIwOCBjMS4xMjUsNC4zNTYsNS4zNTYsNi45Nyw5LjA5LDguOTZjOS4yMDgsNC43MzMsMTkuNTQsNi44NDYsMjkuNjI1LDguNzE0YzI1LjUxMSw0LjExLDUxLjUyNyw0LjIzNSw3Ny4xNjcsMi40ODggYzI3LjE0MS0yLjExNSw1NC4xNDgtNi41OTQsODAuNDExLTE0LjMxM0MzMzcuOTMyLDM2Mi4zNDIsMzMwLjgzNiwzNTguNDc5LDMyNC44NjQsMzUyLjg4eiBNMTg4LjA2OCwzOTUuOTUxIGMtNi45NywxLjk5LTE0LjA2Niw0LjM1Ny0xOS43OSw4Ljk1N2MtMi44NjgsMi4yNDEtNS4xMDUsNi4xMDQtMy43MzQsOS43MTNjMS43NDMsNC42MDQsNi4xLDcuMzQ3LDEwLjIwMyw5LjcwNSBjMTAuNzA4LDUuODU0LDIyLjc4LDguNTg5LDM0LjYwNCwxMC43MDhjMjYuNzY1LDQuMjI5LDU0LjI3LDMuNjA4LDgwLjkwOC0xLjEyYzE1LjgwNi0yLjk4OSwzMS42MTUtNy4yMjEsNDYuMzAxLTEzLjgxNSBjLTkuNTg0LTMuOTg0LTE4LjkxNy04LjQ2Ny0yNy44NzgtMTMuNjkzYy0xNS41NTksMi43MzgtMzEuMjQ2LDUuNjAzLTQ3LjE3OCw2LjU5OGMtMjEuMDMyLDEuNjE4LTQyLjMxOS0wLjEyNS02My4zNTUtMi43MzggYy00Ljk4LTEuMTIxLTExLjIwMi0xLjYxOC0xNC41NjMtNS45NzZDMTgxLjg0Nyw0MDAuNjc3LDE4NS44MjgsMzk4LjA2MywxODguMDY4LDM5NS45NTF6IE0zNTguMzQ1LDQ3NS45ODIgYzE3LjQyNC0zLjYwNCwzNC45NzctNy43MTksNTAuOTA4LTE1LjgwNmM0Ljk3Ni0yLjg2NywxMS4wNzYtNS45NzksMTIuNjk4LTExLjk1YzAuODctNS43MjUtNS4xMDUtOC43MTQtOS4zMzctMTEuMDggYzIuNjEzLDIuOTkzLDQuMzU2LDcuMzQ3LDEuNzQsMTAuODNjLTQuMzU3LDUuODUzLTExLjgyMSw4LjA5MS0xOC40MiwxMC4zMzJjLTIwLjY2LDUuODUtNDIuMDcyLDguMjE2LTYzLjM1NSwxMC41ODIgYy01Ni4zODUsNS4xMDItMTEzLjE0Niw2LjM0OC0xNjkuNTI4LDEuNjE4Yy0xOC45Mi0xLjk5NC0zOC4yMTctNC4xMDktNTYuMjY0LTEwLjgyOWMtMi44Ni0xLjI0Ni03LjIxNy0yLjQ5Mi03LjIxNy02LjM1MiBjMS4xMTctMy4zNTQsNC4zNTctNS4yMjcsNy4yMTctNi44NDVjMTIuOTQ1LTYuNTk1LDI3LjM4NC0xMC4yMDcsNDEuODIyLTExLjA3N2MtNC4yMjgtMi40OTEtOS4yMDgtMi43MzgtMTQuMDYyLTIuNjEzIGMtMTIuMDc2LDAuMzczLTIzLjksMy40ODMtMzUuMzQ5LDcuMzQ3Yy05LjgzNCwzLjYwNC0xOS45MTYsNy41OS0yNy43NiwxNC44MTFjLTMuMTExLDIuNzM1LTUuOTcxLDcuOTYyLTIuNzM5LDExLjY5OSBjNC45OCw1LjM1MywxMi42OTksNi43MiwxOS41NCw4LjMzOGMzOC4zMzgsNi41OTksNzcuMTcxLDEwLjMyOCwxMTYuMDExLDExLjY5OUMyNTUuNzgxLDQ4OC4xODQsMzA3LjY4NCw0ODUuOTQyLDM1OC4zNDUsNDc1Ljk4MnogTTQwOS4zNzgsNDgyLjcwNmMtMjMuNDAyLDcuNDY4LTQ3LjY3MiwxMS41NzQtNzEuODIyLDE0LjkzNmMtNDEuNjk2LDUuMjI3LTgzLjc2OSw2Ljg0NS0xMjUuNzE2LDUuNjAzIGMtMjUuNTE1LTAuOTk1LTUxLjAzLTIuNzM4LTc2LjE3Ni02Ljk3NGM1Ljg1LDMuOTg0LDEzLjA3MSw1LjIyNywxOS43OTQsNy4wOTZjMjguMjU3LDUuOTc2LDU3LjI1NSw3LjA5Niw4Ni4wMSw3Ljk2NiBjNDIuMTksMC43NDgsODQuMzg3LTAuODcsMTI1Ljk2Mi03LjQ2OGMxOS42NjktMy40OCwzOS40NTktNy43MTUsNTcuMTMtMTYuOTI3YzkuMjE1LTQuODU0LDE4LjU1Mi0xMi4zMjYsMjAuMTYzLTIzLjE1MiBDNDM1LjM5MSw0NzMuNzQxLDQyMS45NTEsNDc4LjM0OSw0MDkuMzc4LDQ4Mi43MDZ6Ij4gPC9wYXRoPiA8L2c%2BIDwvZz4KDTwvc3ZnPg%3D%3D
[SpringBoot-url]: https://spring.io
[SpringBoot-badge]: https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Postgres-badge]: https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[Keycloak-url]: https://www.keycloak.org/
[Keycloak-badge]: https://img.shields.io/badge/keycloak-0188ad?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEuMSIgd2lkdGg9IjU0cHgiIGhlaWdodD0iNTRweCIgc3R5bGU9InNoYXBlLXJlbmRlcmluZzpnZW9tZXRyaWNQcmVjaXNpb247IHRleHQtcmVuZGVyaW5nOmdlb21ldHJpY1ByZWNpc2lvbjsgaW1hZ2UtcmVuZGVyaW5nOm9wdGltaXplUXVhbGl0eTsgZmlsbC1ydWxlOmV2ZW5vZGQ7IGNsaXAtcnVsZTpldmVub2RkIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayI%2BCjxnPjxwYXRoIHN0eWxlPSJvcGFjaXR5OjAuOTIxIiBmaWxsPSIjZmVmZmZlIiBkPSJNIDE4LjUsLTAuNSBDIDIzLjgzMzMsLTAuNSAyOS4xNjY3LC0wLjUgMzQuNSwtMC41QyA0My44MzMzLDIuODMzMzMgNTAuMTY2Nyw5LjE2NjY3IDUzLjUsMTguNUMgNTMuNSwyMy44MzMzIDUzLjUsMjkuMTY2NyA1My41LDM0LjVDIDUwLjE2NjcsNDMuODMzMyA0My44MzMzLDUwLjE2NjcgMzQuNSw1My41QyAyOS4xNjY3LDUzLjUgMjMuODMzMyw1My41IDE4LjUsNTMuNUMgOS4xNjY2Nyw1MC4xNjY3IDIuODMzMzMsNDMuODMzMyAtMC41LDM0LjVDIC0wLjUsMjkuMTY2NyAtMC41LDIzLjgzMzMgLTAuNSwxOC41QyAyLjgzMzMzLDkuMTY2NjcgOS4xNjY2NywyLjgzMzMzIDE4LjUsLTAuNSBaIE0gMTkuNSwxNC41IEMgMjMuODQ2MSwxNC4zMzQzIDI4LjE3OTQsMTQuNTAxIDMyLjUsMTVDIDMzLjg3MjEsMTYuMjQxOSAzNC44NzIxLDE3Ljc0MTkgMzUuNSwxOS41QyAzNy4wNDUzLDIwLjQ1MTkgMzguNzEyLDIwLjc4NTIgNDAuNSwyMC41QyA0MC41LDI0LjUgNDAuNSwyOC41IDQwLjUsMzIuNUMgMzguNzEyLDMyLjIxNDggMzcuMDQ1MywzMi41NDgxIDM1LjUsMzMuNUMgMzQuODcyMSwzNS4yNTgxIDMzLjg3MjEsMzYuNzU4MSAzMi41LDM4QyAyOC4xNjY3LDM4LjY2NjcgMjMuODMzMywzOC42NjY3IDE5LjUsMzhDIDE3LjA0MTEsMzQuMjUgMTQuNzA3OCwzMC40MTY2IDEyLjUsMjYuNUMgMTQuODQ1LDIyLjQ3MjUgMTcuMTc4MywxOC40NzI1IDE5LjUsMTQuNSBaIi8%2BPC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eTowLjcwNiIgZmlsbD0iI2ZlZmZmZSIgZD0iTSAyMC41LDE3LjUgQyAyMi43MTU5LDE3LjI3NDYgMjQuMzgyNiwxOC4xMDc5IDI1LjUsMjBDIDIxLjUsMjQuMzMzMyAyMS41LDI4LjY2NjcgMjUuNSwzM0MgMjMuMTkwOSwzNi4yODU2IDIxLjAyNDMsMzYuMTE4OSAxOSwzMi41QyAxNy44ODc3LDMwLjYwOTYgMTcuMDU0NCwyOC42MDk2IDE2LjUsMjYuNUMgMTcuNTg1LDIzLjMzNTEgMTguOTE4MywyMC4zMzUxIDIwLjUsMTcuNSBaIi8%2BPC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eTowLjcwNyIgZmlsbD0iI2ZlZmZmZSIgZD0iTSAyNy41LDE3LjUgQyAzNy4xODc0LDIyLjI5ODUgMzcuODU0MSwyOC4yOTg1IDI5LjUsMzUuNUMgMjcuODMzMywzNS4xNjY3IDI2LjgzMzMsMzQuMTY2NyAyNi41LDMyLjVDIDMwLjUsMjguNSAzMC41LDI0LjUgMjYuNSwyMC41QyAyNy4xMjcyLDE5LjU4MzIgMjcuNDYwNiwxOC41ODMyIDI3LjUsMTcuNSBaIi8%2BPC9nPgo8L3N2Zz4K
[Docker-url]: https://www.docker.com/
[Docker-badge]: https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[TestContainers-url]: https://testcontainers.com/
[TestContainers-badge]: https://img.shields.io/badge/testcontainers-09CFBB?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPCEtLSBHZW5lcmF0b3I6IEFkb2JlIElsbHVzdHJhdG9yIDI2LjIuMSwgU1ZHIEV4cG9ydCBQbHVnLUluIC4gU1ZHIFZlcnNpb246IDYuMDAgQnVpbGQgMCkgIC0tPgo8c3ZnIHZlcnNpb249IjEuMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayIgeD0iMHB4IiB5PSIwcHgiCgkgdmlld0JveD0iMCAwIDI2MyAzMDAiIHN0eWxlPSJlbmFibGUtYmFja2dyb3VuZDpuZXcgMCAwIDI2MyAzMDA7IiB4bWw6c3BhY2U9InByZXNlcnZlIj4KPHN0eWxlIHR5cGU9InRleHQvY3NzIj4KCS5zdDB7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMjkxQTNGO30KCS5zdDF7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMUQxMTMwO30KCS5zdDJ7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzFfKTt9Cgkuc3Qze2ZpbGwtcnVsZTpldmVub2RkO2NsaXAtcnVsZTpldmVub2RkO2ZpbGw6dXJsKCNTVkdJRF8wMDAwMDEwMTA5OTU4Njk0NDM2ODE0NTcyMDAwMDAwMzU0NjcyOTc2MTA0OTMyMzcxMF8pO30KCS5zdDR7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojRkY4NzM2O30KCS5zdDV7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojRjU2MDc5O30KCS5zdDZ7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMDlDRkJCO30KCS5zdDd7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMkJBRkFDO30KCS5zdDh7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMDBBMUIyO30KCS5zdDl7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMzYxRTVCO30KCS5zdDEwe2ZpbGwtcnVsZTpldmVub2RkO2NsaXAtcnVsZTpldmVub2RkO2ZpbGw6IzQ0MzI3Qzt9Cgkuc3QxMXtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOiM5OTkxQjU7fQoJLnN0MTJ7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojQjZDMURCO30KCS5zdDEze2ZpbGwtcnVsZTpldmVub2RkO2NsaXAtcnVsZTpldmVub2RkO2ZpbGw6I0RGRTRGNDt9Cgkuc3QxNHtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOiNFQkVGRkE7fQoJLnN0MTV7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojRkZGRkZGO30KCS5zdDE2e2ZpbGw6I0ZGRkZGRjt9Cgkuc3QxN3tmaWxsOiMwQzk0QUE7fQoJLnN0MTh7ZmlsbDojMkJBRkFDO30KCS5zdDE5e2ZpbGw6IzA5Q0ZCQjt9Cgkuc3QyMHtmaWxsOiMyOTFBM0Y7fQoJLnN0MjF7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMDUyODE2MTg3MDcyNDAzOTE5NjEwMDAwMDA4NjA3MTI3MTc3MzM3MzQyMTA4Xyk7fQoJLnN0MjJ7ZmlsbDojMjMxRjIzO30KCS5zdDIze2Rpc3BsYXk6bm9uZTt9Cgkuc3QyNHtkaXNwbGF5OmlubGluZTtmaWxsOiNGN0Y5RkQ7fQoJLnN0MjV7ZmlsbDp1cmwoI1BhdGhfMTQ3LTJfMDAwMDAwNzUxNDM0OTIxMzIzODI4ODk0MzAwMDAwMTA3ODU4OTU0OTY2NzY5MzcwODlfKTt9Cgkuc3QyNntmaWxsOm5vbmU7fQoJLnN0Mjd7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMDI1NDQzMjcxMDE4OTk3MjI5NjAwMDAwMDA2MDk3MDcxNTIwMjgwNTUwODEwXyk7fQoJLnN0Mjh7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMDQ3NzgyNzM3Mjg0OTU0NDM5MzAwMDAwMDAyMzk5Nzk1MDA2Njc4NzM2Mjg2Xyk7fQoJLnN0Mjl7ZmlsbDp1cmwoI1BhdGhfMTQ3LTJfMDAwMDAwNDEyNjk0MjcxMjcyMDY3Nzk4OTAwMDAwMDUwNTIwNzcxNTAyNDcyMTczMTRfKTt9Cgkuc3QzMHtkaXNwbGF5OmlubGluZTtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOm5vbmU7fQoJLnN0MzF7ZGlzcGxheTppbmxpbmU7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMTE2MjMzMjQ4MTU5NDY0MTU2NjYwMDAwMDAwNDU2MTE0MzQyNDIxMzUwMDU1Xyk7fQoJLnN0MzJ7ZGlzcGxheTppbmxpbmU7b3BhY2l0eTowLjU7ZmlsbDojMDlDRkJCO30KCS5zdDMze2Rpc3BsYXk6aW5saW5lO30KCS5zdDM0e2NsaXAtcGF0aDp1cmwoI1NWR0lEXzAwMDAwMTE2MjAyODA3ODM4NzA4Mzc1NDAwMDAwMDA2NzY4NzQyMzgwNTExMTM0NjQ4Xyk7fQoJLnN0MzV7ZmlsbDojMTZDMUJEO30KCS5zdDM2e2Rpc3BsYXk6aW5saW5lO2ZpbGw6I0ZGRkZGRjt9Cgkuc3QzN3tmaWxsOiMzNjFFNUI7fQoJLnN0Mzh7Y2xpcC1wYXRoOnVybCgjU1ZHSURfMDAwMDAwOTI0MzkzNjM5Njk3MjA4MzUxMTAwMDAwMDMyNzAzMjM2NjczNjcwMTc4OTJfKTt9Cgkuc3QzOXtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOnVybCgjU1ZHSURfMDAwMDAxNDI4NzAwOTUwMDU4NDQ2MTk1NTAwMDAwMTIyNTU2NTgzNDQwNjM1MzE5NjRfKTt9Cgkuc3Q0MHtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOnVybCgjU1ZHSURfMDAwMDAxNzA5Njc4NTc5MDk1MDk0OTAwNjAwMDAwMTE5MzE5NTIwMTA1OTg5MjUyMDNfKTt9Cgkuc3Q0MXtmaWxsOnVybCgjUGF0aF8xNDctMl8wMDAwMDAwMzEwNjc4MjM2MDgwMDI4NTU0MDAwMDAwMzAxOTM1MTAyNDg1NzAyODI3Nl8pO30KCS5zdDQye2ZpbGw6IzhCQ0NCMDt9Cgkuc3Q0M3tmaWxsOiM1M0EzQTY7fQoJLnN0NDR7ZmlsbDojMzE4QkEyO30KCS5zdDQ1e2ZpbGwtcnVsZTpldmVub2RkO2NsaXAtcnVsZTpldmVub2RkO2ZpbGw6IzhCQ0NCMDt9Cgkuc3Q0NntmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOiM1M0EzQTY7fQoJLnN0NDd7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDojMzE4QkEyO30KCS5zdDQ4e29wYWNpdHk6MC41O30KCS5zdDQ5e2ZpbGw6bm9uZTtzdHJva2U6Izk5OTFCNTtzdHJva2Utd2lkdGg6Mi42MzU1O3N0cm9rZS1taXRlcmxpbWl0OjEwO30KCS5zdDUwe2ZpbGw6Izk5OTFCNTt9Cgkuc3Q1MXtmaWxsLXJ1bGU6ZXZlbm9kZDtjbGlwLXJ1bGU6ZXZlbm9kZDtmaWxsOiMwQzk0QUE7fQoJLnN0NTJ7ZmlsbDojM0UyRjVCO30KCS5zdDUze2NsaXAtcGF0aDp1cmwoI1NWR0lEXzAwMDAwMTQ4NjUyODEzMTAzMDQxMTkyMjMwMDAwMDA4MDkwMjE1ODEyMzI0MzA3MzQ2Xyk7fQoJLnN0NTR7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMTYwMTcxODUwNzMxNTc2NDIwOTUwMDAwMDE3NTc2ODcwMTUwMzcwMDU5NjUwXyk7fQoJLnN0NTV7ZmlsbC1ydWxlOmV2ZW5vZGQ7Y2xpcC1ydWxlOmV2ZW5vZGQ7ZmlsbDp1cmwoI1NWR0lEXzAwMDAwMTU0Mzg0MTY5OTU4Njg0MDE2MjQwMDAwMDE3MjE1MjM0OTY2Mjk4NjAxNDAzXyk7fQoJLnN0NTZ7ZmlsbDp1cmwoI1BhdGhfMTQ3LTJfMDAwMDAwNjY1MDIzNjY3NjYzNTgxNTYxOTAwMDAwMDU5NDkzMzY3ODU5OTE3MTUyMDJfKTt9Cjwvc3R5bGU%2BCjxnIGlkPSJMYXllcl8zIj4KCTxnPgoJCTxwYXRoIGNsYXNzPSJzdDE2IiBkPSJNMjYyLjgsODFjMC0wLjIsMC0wLjQsMC0wLjdjMC0wLjYtMC4xLTEuMi0wLjItMS44Yy0wLjUtMy4yLTIuNS01LjktNS4zLTcuNmMtMC4zLTAuMS0wLjUtMC4zLTAuOC0wLjQKCQkJTDEzNywxLjVjLTMuNC0xLjktNy41LTEuOS0xMC44LDBMNS42LDcxYy0zLjQsMS45LTUuNCw1LjUtNS40LDkuNEwwLjEsMjE5LjVjMCwzLjksMi4xLDcuNCw1LjQsOS40TDEyNiwyOTguNQoJCQljMS42LDEsMy41LDEuNSw1LjQsMS41aDAuMmMxLjksMCwzLjgtMC41LDUuNC0xLjVsMTIwLjUtNjkuNmMzLjQtMS45LDUuNC01LjUsNS40LTkuNEwyNjIuOCw4MXoiLz4KCQk8Zz4KCQkJPHBvbHlnb24gY2xhc3M9InN0MTciIHBvaW50cz0iMjM5LjYsODcuNiAyMzkuNiwyMTIuNCAxMzEuNSwyNzQuOCAxMzEuNSwxNTAgCQkJIi8%2BCgkJCTxwb2x5Z29uIGNsYXNzPSJzdDE4IiBwb2ludHM9IjIzOS42LDg3LjYgMTMxLjUsMTUwIDIzLjQsODcuNiAxMzEuNSwyNS4yIAkJCSIvPgoJCQk8cG9seWdvbiBjbGFzcz0ic3QxOSIgcG9pbnRzPSIxMzEuNSwxNTAgMTMxLjUsMjc0LjggMjMuNCwyMTIuNCAyMy40LDg3LjYgCQkJIi8%2BCgkJPC9nPgoJPC9nPgo8L2c%2BCjxnIGlkPSJMYXllcl84Ij4KPC9nPgo8ZyBpZD0iTGF5ZXJfNyIgY2xhc3M9InN0MjMiPgo8L2c%2BCjxnIGlkPSJMYXllcl82Ij4KPC9nPgo8ZyBpZD0iTGF5ZXJfNCIgY2xhc3M9InN0MjMiPgo8L2c%2BCjxnIGlkPSJMYXllcl81IiBjbGFzcz0ic3QyMyI%2BCjwvZz4KPGcgaWQ9IkxheWVyXzIiIGNsYXNzPSJzdDIzIj4KPC9nPgo8ZyBpZD0iTGF5ZXJfMSI%2BCjwvZz4KPGcgaWQ9IkxheWVyXzkiPgo8L2c%2BCjwvc3ZnPgo%3D

