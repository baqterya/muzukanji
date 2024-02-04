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
[![MIT License][license-shield]][license-url] [![Actions CI](https://github.com/baqterya/muzukanji/actions/workflows/ci.yml/badge.svg)](https://github.com/baqterya/muzukanji/actions/workflows/ci.yml)
<br/>
A Spring Boot API that stores 13108 Kanji characters. The records contain each kanji's meanings,
possible readings in both kana and romaji, the number of strokes, JLPT level, Jyoyo Grade and
newspaper frequency if applicable. It uses PostgreSQL as a database and Keycloak to secure the protected endpoints.
<br />
<a href="#usage" placeholder="Work in progress"><strong>Explore the docs »</strong></a>

<br />
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#local-setup">Local Setup</a></li>
      </ul>
    </li>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#open-endpoints">Open Endpoints</a></li>
        <li><a href="#protected-endpoints">Protected Endpoints</a></li>
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


* [Docker](https://www.docker.com/)

### Local Setup

1. Navigate to the root directory.
2. Create a ``.env`` file that will store all the sensitive data (replace the fields marked ith * with the corresponding data):
   ```sh
   echo POSTGRES_USER=*database username* > .env
   echo POSTGRES_PASS=*database password* > .env
   echo KEYCLOAK_USER=*keycloak username* > .env
   echo KEYCLOAK_PASS=*keycloak password* > .env
   echo PGADMIN_EMAIL=*pgadmin username* > .env
   echo PGADMIN_PASS=*pgadmin password* > .env
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
[Java-badge]: https://img.shields.io/badge/java-ffa500?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iaXNvLTg4NTktMSI%2FPg0KPCEtLSBVcGxvYWRlZCB0bzogU1ZHIFJlcG8sIHd3dy5zdmdyZXBvLmNvbSwgR2VuZXJhdG9yOiBTVkcgUmVwbyBNaXhlciBUb29scyAtLT4NCjxzdmcgaGVpZ2h0PSI4MDBweCIgd2lkdGg9IjgwMHB4IiB2ZXJzaW9uPSIxLjEiIGlkPSJDYXBhXzEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiIA0KCSB2aWV3Qm94PSIwIDAgNTAyLjYzMiA1MDIuNjMyIiB4bWw6c3BhY2U9InByZXNlcnZlIj4NCjxnPg0KCTxnPg0KCQk8cGF0aCBzdHlsZT0iZmlsbDojRkZGRkZGOyIgZD0iTTI0MC44NjQsMjY5Ljg5NGMwLDAtMjguMDItNTMuOTkyLTI2Ljk4NS05My40NDVjMC43NTUtMjguMTkzLDY0LjMyNC01Ni4wNjIsODkuMjgxLTk2LjUyOQ0KCQkJQzMyOC4wNzQsMzkuNDMxLDMwMC4wNTQsMCwzMDAuMDU0LDBzNi4yMzQsMjkuMDc3LTEwLjM3Niw1OS4xNDdjLTE2LjYwOSwzMC4xMTMtNzcuOTE0LDQ3Ljc3OS0xMDEuNzQ5LDk5LjY3OQ0KCQkJUzI0MC44NjQsMjY5Ljg5NCwyNDAuODY0LDI2OS44OTR6Ii8%2BDQoJCTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMzQ1Ljc0MSwxMDUuODY5YzAsMC05NS40OTQsMzYuMzQ3LTk1LjQ5NCw3Ny44NDljMCw0MS41NDUsMjUuOTI4LDU1LjAyNywzMC4xMTMsNjguNTA5DQoJCQljNC4xNDIsMTMuNTI1LTcuMjY5LDM2LjM0Ny03LjI2OSwzNi4zNDdzMzcuMzYxLTI1Ljk1LDMxLjEwNS01Ni4wNjJjLTYuMjM0LTMwLjExMy0zNS4yOS0zOS40NzUtMTguNjU5LTY5LjU0NA0KCQkJQzI5Ni42NDYsMTQyLjc5OSwzNDUuNzQxLDEwNS44NjksMzQ1Ljc0MSwxMDUuODY5eiIvPg0KCQk8cGF0aCBzdHlsZT0iZmlsbDojRkZGRkZGOyIgZD0iTTIzMC41MSwzMjQuNzQ4Yzg4LjI0Ni0zLjE0OSwxMjAuNDMtMzAuOTk3LDEyMC40My0zMC45OTcNCgkJCWMtNTcuMDc2LDE1LjU1My0yMDguNjU0LDE0LjUzOS0yMDkuNzExLDMuMTI4Yy0xLjAxNC0xMS40MTEsNDYuNzAxLTIwLjc3Myw0Ni43MDEtMjAuNzczcy03NC43MjEsMC04MC45NTUsMTguNjgNCgkJCUMxMDAuNzQsMzEzLjQ2NywxNDIuMzI4LDMyNy44MzMsMjMwLjUxLDMyNC43NDh6Ii8%2BDQoJCTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMzU4LjE4NywzNjguNDk0YzAsMCw4Ni4zNjktMTguNDIxLDc3LjgyNy02NS4zMzhjLTEwLjM1NC01Ny4xMTktNzAuNTgtMjQuOTM2LTcwLjU4LTI0LjkzNg0KCQkJczQyLjYwMiwwLDQ2LjcyMiwyNS45MjhDNDE2LjMyLDMzMC4wOTgsMzU4LjE4NywzNjguNDk0LDM1OC4xODcsMzY4LjQ5NHoiLz4NCgkJPHBhdGggc3R5bGU9ImZpbGw6I0ZGRkZGRjsiIGQ9Ik0zMTUuNjI4LDM0My42MDFjMCwwLTIxLjc2NSw1LjcxNi01NC4wMTMsOS4zNGMtNDMuMjI4LDQuODUzLTk1LjQ5NCwxLjAxNC05OS42NTctNi4yNTYNCgkJCWMtNC4wOTgtNy4yNjksNy4yNjktMTEuNDExLDcuMjY5LTExLjQxMWMtNTEuOTIxLDEyLjQ2OC0yMy41MTIsMzQuMjMzLDM3LjMzOSwzOC40MThjNTIuMTU4LDMuNTU5LDEyOS43OTEtMTUuNTc0LDEyOS43OTEtMTUuNTc0DQoJCQlMMzE1LjYyOCwzNDMuNjAxeiIvPg0KCQk8cGF0aCBzdHlsZT0iZmlsbDojRkZGRkZGOyIgZD0iTTE4MS43MzgsMzg4Ljk0M2MwLDAtMjMuNTU1LDAuNjY5LTI0LjkzNiwxMy4xMzdjLTEuMzU5LDEyLjM4MiwxNC40OTYsMjMuNTEyLDcyLjY1LDI2Ljk2NA0KCQkJYzU4LjEzMywzLjQ1MSw5OC45ODgtMTUuODk4LDk4Ljk4OC0xNS44OThsLTI2LjI5NS0xNS45NjJjMCwwLTE2LjYzMSwzLjQ5NC00Mi4yMzYsNi45NDYNCgkJCWMtMjUuNjI2LDMuNDczLTc4LjE3My0yLjc4My04MC4yNDMtNy41OTNDMTc3LjU1MywzOTEuNjgyLDE4MS43MzgsMzg4Ljk0MywxODEuNzM4LDM4OC45NDN6Ii8%2BDQoJCTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNNDA3Ljk5NCw0NDUuMDA1YzguOTk1LTkuNzA3LTIuNzgzLTE3LjMyMS0yLjc4My0xNy4zMjFzNC4xNDIsNC44NTMtMS4zMzcsMTAuMzc2DQoJCQljLTUuNTQ0LDUuNTIyLTU2LjA4NCwxOS4zNDktMTM3LjA2MSwyMy41MTJjLTgwLjk1NSw0LjE2My0xNjguODU2LTcuNjE1LTE3MS42MzktMTcuOTkNCgkJCWMtMi42OTYtMTAuMzc2LDQ1LjAxOC0xOC42NTksNDUuMDE4LTE4LjY1OWMtNS41MjIsMC42OS03MS45NiwyLjA3MS03NC4wNzQsMjAuMDgyYy0yLjA3MSwxNy45NjgsMjkuMDU2LDMyLjUwNywxNTMuNjcsMzIuNTA3DQoJCQlDMzQ0LjMzOSw0NzcuNDkxLDM5OS4wNDIsNDU0LjY0Nyw0MDcuOTk0LDQ0NS4wMDV6Ii8%2BDQoJCTxwYXRoIHN0eWxlPSJmaWxsOiNGRkZGRkY7IiBkPSJNMzU5LjU2OCw0ODUuODE3Yy01NC42ODIsMTEuMDQ0LTIyMC43MzQsNC4wNzctMjIwLjczNCw0LjA3N3MxMDcuOTE5LDI1LjYyNiwyMzEuMTA5LDQuMTg1DQoJCQljNTguODg4LTEwLjI2OCw2Mi4zMTgtMzguNzYzLDYyLjMxOC0zOC43NjNTNDE0LjI1LDQ3NC43MDgsMzU5LjU2OCw0ODUuODE3eiIvPg0KCTwvZz4NCgk8Zz4NCgk8L2c%2BDQoJPGc%2BDQoJPC9nPg0KCTxnPg0KCTwvZz4NCgk8Zz4NCgk8L2c%2BDQoJPGc%2BDQoJPC9nPg0KCTxnPg0KCTwvZz4NCgk8Zz4NCgk8L2c%2BDQoJPGc%2BDQoJPC9nPg0KCTxnPg0KCTwvZz4NCgk8Zz4NCgk8L2c%2BDQoJPGc%2BDQoJPC9nPg0KCTxnPg0KCTwvZz4NCgk8Zz4NCgk8L2c%2BDQoJPGc%2BDQoJPC9nPg0KCTxnPg0KCTwvZz4NCjwvZz4NCjwvc3ZnPg%3D%3D

[SpringBoot-url]: https://spring.io
[SpringBoot-badge]: https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Postgres-badge]: https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[Keycloak-url]: https://www.keycloak.org/
[Keycloak-badge]: https://img.shields.io/badge/keycloak-0188ad?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEuMSIgd2lkdGg9IjU0cHgiIGhlaWdodD0iNTRweCIgc3R5bGU9InNoYXBlLXJlbmRlcmluZzpnZW9tZXRyaWNQcmVjaXNpb247IHRleHQtcmVuZGVyaW5nOmdlb21ldHJpY1ByZWNpc2lvbjsgaW1hZ2UtcmVuZGVyaW5nOm9wdGltaXplUXVhbGl0eTsgZmlsbC1ydWxlOmV2ZW5vZGQ7IGNsaXAtcnVsZTpldmVub2RkIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayI%2BCjxnPjxwYXRoIHN0eWxlPSJvcGFjaXR5OjAuOTIxIiBmaWxsPSIjZmVmZmZlIiBkPSJNIDE4LjUsLTAuNSBDIDIzLjgzMzMsLTAuNSAyOS4xNjY3LC0wLjUgMzQuNSwtMC41QyA0My44MzMzLDIuODMzMzMgNTAuMTY2Nyw5LjE2NjY3IDUzLjUsMTguNUMgNTMuNSwyMy44MzMzIDUzLjUsMjkuMTY2NyA1My41LDM0LjVDIDUwLjE2NjcsNDMuODMzMyA0My44MzMzLDUwLjE2NjcgMzQuNSw1My41QyAyOS4xNjY3LDUzLjUgMjMuODMzMyw1My41IDE4LjUsNTMuNUMgOS4xNjY2Nyw1MC4xNjY3IDIuODMzMzMsNDMuODMzMyAtMC41LDM0LjVDIC0wLjUsMjkuMTY2NyAtMC41LDIzLjgzMzMgLTAuNSwxOC41QyAyLjgzMzMzLDkuMTY2NjcgOS4xNjY2NywyLjgzMzMzIDE4LjUsLTAuNSBaIE0gMTkuNSwxNC41IEMgMjMuODQ2MSwxNC4zMzQzIDI4LjE3OTQsMTQuNTAxIDMyLjUsMTVDIDMzLjg3MjEsMTYuMjQxOSAzNC44NzIxLDE3Ljc0MTkgMzUuNSwxOS41QyAzNy4wNDUzLDIwLjQ1MTkgMzguNzEyLDIwLjc4NTIgNDAuNSwyMC41QyA0MC41LDI0LjUgNDAuNSwyOC41IDQwLjUsMzIuNUMgMzguNzEyLDMyLjIxNDggMzcuMDQ1MywzMi41NDgxIDM1LjUsMzMuNUMgMzQuODcyMSwzNS4yNTgxIDMzLjg3MjEsMzYuNzU4MSAzMi41LDM4QyAyOC4xNjY3LDM4LjY2NjcgMjMuODMzMywzOC42NjY3IDE5LjUsMzhDIDE3LjA0MTEsMzQuMjUgMTQuNzA3OCwzMC40MTY2IDEyLjUsMjYuNUMgMTQuODQ1LDIyLjQ3MjUgMTcuMTc4MywxOC40NzI1IDE5LjUsMTQuNSBaIi8%2BPC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eTowLjcwNiIgZmlsbD0iI2ZlZmZmZSIgZD0iTSAyMC41LDE3LjUgQyAyMi43MTU5LDE3LjI3NDYgMjQuMzgyNiwxOC4xMDc5IDI1LjUsMjBDIDIxLjUsMjQuMzMzMyAyMS41LDI4LjY2NjcgMjUuNSwzM0MgMjMuMTkwOSwzNi4yODU2IDIxLjAyNDMsMzYuMTE4OSAxOSwzMi41QyAxNy44ODc3LDMwLjYwOTYgMTcuMDU0NCwyOC42MDk2IDE2LjUsMjYuNUMgMTcuNTg1LDIzLjMzNTEgMTguOTE4MywyMC4zMzUxIDIwLjUsMTcuNSBaIi8%2BPC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eTowLjcwNyIgZmlsbD0iI2ZlZmZmZSIgZD0iTSAyNy41LDE3LjUgQyAzNy4xODc0LDIyLjI5ODUgMzcuODU0MSwyOC4yOTg1IDI5LjUsMzUuNUMgMjcuODMzMywzNS4xNjY3IDI2LjgzMzMsMzQuMTY2NyAyNi41LDMyLjVDIDMwLjUsMjguNSAzMC41LDI0LjUgMjYuNSwyMC41QyAyNy4xMjcyLDE5LjU4MzIgMjcuNDYwNiwxOC41ODMyIDI3LjUsMTcuNSBaIi8%2BPC9nPgo8L3N2Zz4K
[Docker-url]: https://www.docker.com/
[Docker-badge]: https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[TestContainers-url]: https://testcontainers.com/
[TestContainers-badge]: https://img.shields.io/badge/testcontainers-09cfbb?style=for-the-badge&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPCFET0NUWVBFIHN2ZyBQVUJMSUMgIi0vL1czQy8vRFREIFNWRyAxLjEvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvR3JhcGhpY3MvU1ZHLzEuMS9EVEQvc3ZnMTEuZHRkIj4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZlcnNpb249IjEuMSIgd2lkdGg9IjI2M3B4IiBoZWlnaHQ9IjI5OXB4IiBzdHlsZT0ic2hhcGUtcmVuZGVyaW5nOmdlb21ldHJpY1ByZWNpc2lvbjsgdGV4dC1yZW5kZXJpbmc6Z2VvbWV0cmljUHJlY2lzaW9uOyBpbWFnZS1yZW5kZXJpbmc6b3B0aW1pemVRdWFsaXR5OyBmaWxsLXJ1bGU6ZXZlbm9kZDsgY2xpcC1ydWxlOmV2ZW5vZGQiIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4KPGc%2BPHBhdGggc3R5bGU9Im9wYWNpdHk6MC45ODMiIGZpbGw9IiNmZWZlZmUiIGQ9Ik0gMTI1LjUsLTAuNSBDIDEyOS4xNjcsLTAuNSAxMzIuODMzLC0wLjUgMTM2LjUsLTAuNUMgMTc4Ljc2MywyNC40MzU4IDIyMC43NjMsNDkuNzY5MiAyNjIuNSw3NS41QyAyNjIuNSwxMjUuNSAyNjIuNSwxNzUuNSAyNjIuNSwyMjUuNUMgMjIwLjgzLDI0OS4zMzIgMTc5LjQ5NiwyNzMuNjY2IDEzOC41LDI5OC41QyAxMzQuNSwyOTguNSAxMzAuNSwyOTguNSAxMjYuNSwyOTguNUMgODQuMzM3NywyNzMuOTE3IDQyLjAwNDQsMjQ5LjU4NCAtMC41LDIyNS41QyAtMC41LDE3NS41IC0wLjUsMTI1LjUgLTAuNSw3NS41QyAyLjE2MzkyLDcxLjk4MzcgNS40OTcyNSw2OS4xNTAzIDkuNSw2N0MgNDguMzkzNSw0NC43MjU4IDg3LjA2MDIsMjIuMjI1OCAxMjUuNSwtMC41IFoiLz48L2c%2BCjxnPjxwYXRoIHN0eWxlPSJvcGFjaXR5OjEiIGZpbGw9IiMyZGFmYWMiIGQ9Ik0gMjM4LjUsMjEyLjUgQyAyMzguNjY3LDE3MC44MzIgMjM4LjUsMTI5LjE2NSAyMzgsODcuNUMgMjAyLjY2NywxMDguMTY3IDE2Ny4zMzMsMTI4LjgzMyAxMzIsMTQ5LjVDIDEzMS41LDE5MC44MzIgMTMxLjMzMywyMzIuMTY1IDEzMS41LDI3My41QyAxMzEuMTY3LDI3NC44MzMgMTMwLjgzMywyNzQuODMzIDEzMC41LDI3My41QyAxMzAuODMzLDIzMS44MjggMTMwLjUsMTkwLjE2MSAxMjkuNSwxNDguNUMgOTMuODY3NywxMjguMTY5IDU4LjUzNDMsMTA3LjUwMyAyMy41LDg2LjVDIDU5LjM4NzYsNjUuOTcxNCA5NS4yMjA5LDQ1LjMwNDcgMTMxLDI0LjVDIDE2Ni43NTIsNDUuMjkzIDIwMi41ODUsNjUuOTU5NyAyMzguNSw4Ni41QyAyMzkuODMxLDEyOC42NjUgMjM5LjgzMSwxNzAuNjY1IDIzOC41LDIxMi41IFoiLz48L2c%2BCjxnPjxwYXRoIHN0eWxlPSJvcGFjaXR5OjEiIGZpbGw9IiMwZGNmYmIiIGQ9Ik0gMjMuNSw4Ni41IEMgNTguNTM0MywxMDcuNTAzIDkzLjg2NzcsMTI4LjE2OSAxMjkuNSwxNDguNUMgMTMwLjUsMTkwLjE2MSAxMzAuODMzLDIzMS44MjggMTMwLjUsMjczLjVDIDk0LjcyNTQsMjUzLjQ0MyA1OS4wNTg3LDIzMy4xMSAyMy41LDIxMi41QyAyMi4xNjkzLDE3MC4zMzUgMjIuMTY5MywxMjguMzM1IDIzLjUsODYuNSBaIi8%2BPC9nPgo8Zz48cGF0aCBzdHlsZT0ib3BhY2l0eToxIiBmaWxsPSIjMGM5NGFhIiBkPSJNIDIzOC41LDIxMi41IEMgMjAyLjk2OSwyMzMuMDk4IDE2Ny4zMDIsMjUzLjQzMiAxMzEuNSwyNzMuNUMgMTMxLjMzMywyMzIuMTY1IDEzMS41LDE5MC44MzIgMTMyLDE0OS41QyAxNjcuMzMzLDEyOC44MzMgMjAyLjY2NywxMDguMTY3IDIzOCw4Ny41QyAyMzguNSwxMjkuMTY1IDIzOC42NjcsMTcwLjgzMiAyMzguNSwyMTIuNSBaIi8%2BPC9nPgo8L3N2Zz4K


