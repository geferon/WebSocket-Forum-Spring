
## What is this?
This is my final project for my web app development degree (Ciclo Superior de Desarrollo de Aplicaciones Web) for 2021.

Some of the code of the project, including comments and also the web interface, is in Spanish due to getting this degree in Spain.

**Note:**
I was forced to use Java and Spring for backend (which I hate), and I just did most of the stuff as quickly as I could instead of doing it the right way, so expect to see some bad code around.
I do not intend to create a forum from this project or to develop on it further, this was just an idea I wanted to experiment on for my final project.

## Project description
This project is basically a live web forum based on previous sites I've used (such as the old Facepunch forums or the Knockout forums).
The only difference with most forums is that this forum makes use of websockets to show live posts, etc. If you're using the forum and a person posts on the thread/category you're in, or even the home page (any page, really), it will instantly show the new posts created (or even when they're edited).
There's an available demo in https://foro.geferon.net/

## Current features
 - Home page with category listings (each showing their last post as well)
 - Categories with pagination
 - Threads with pagination
 - Posts use markdown for text formatting
 - User login and registration
 - User settings and the ability for users to change their profile pictures (even to animated GIFs)
 - Admin accounts that can edit, create and delete categories, as well as posts and threads
 - Live preview of everything that is happening on the website.

## Technologies used
**Backend:**
 - Spring
 - JPA and Hibernate
 - JWT Auth (done manually)
 - Web sockets (via SockJS)

**Frontend:**
 - Angular 12
 - Angular Material
 - SockJS
 - Toast-UI Editor (for a Markdown editor and viewer)
There might be some other techs I might've used in the project that I have forgotten about.