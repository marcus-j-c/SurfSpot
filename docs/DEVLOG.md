# SurfSpot Development Log

## Day 1

Development environment setup completed.

Installed and configured:
- Java Development Kit (JDK 17)
- IntelliJ IDEA Ultimate
- Visual Studio Code
- Node.js and npm
- Docker Desktop
- PostgreSQL
- pgAdmin 4
- Bruno API client

Configured:
- Git and GitHub integration
- GitHub Education verification
- Project repository structure
- Frontend, backend, API testing, and documentation directories

Created initial SurfSpot repository and connected local development environment.

Created initial Spring Boot backend application, that runs successfully in the terminal, and is available at localhost:8080.

Created my first 2 controllers, just test ones, a basic rest controller, and one that takes parameters.

Used Bruno to send my first requests without having to actually use the front end.

## Day 2

Created my first JSON returning controllers, and used @RequestMapping for the first time.

Then discovered Java Record Response which takes way less code and works the same!!

Removed Beach class to instead use BeachInfo class that uses Java Record Response as it is sooooo much better!

Set up my React Vite TypeScript frontend!

First TypeScript written and tested my first HTML tags in app.tsx, first colours also added using app.css.

## Day 3

First react components created and used, along with deeper understanding of app.css and html stuff gained!

Created my first props in the forecast card component!

Used useState for the first time along with onChange and onClick to make a text entry box that when you click the button next to it, send an alert that says what you typed into the box!

Created my first SearchBarProps interface to define what stuff my search bar component accepts.

Learned how child components like my SearchBar can pass data back UP to parent components, in this case App.tsx.

Passed handleSearch from App.tsx as a prop into <SearchBar onSearch={handleSearch}/> so clicking Search in the child component updates setSelectedBeach in App.tsx!!!

Understood how functions can be passed around like variables in TypeScript (how cool!).

Used flexboxes for the first time.

## Day 4
Begin proper fake Surfspot UI today.

Moved a few things around using props and lifting up to enable better CSS design.

Styled my first search bar, but its still needs a whole lot of work.

Search bar is looking great, and ive got a centre header now that also looks good.

Installed then uninstalled lucide react when i clocked that the icons sucked.

Added basic account buttons, time to style them!!

Made those basic account buttons look really cool with a glowing gradient animation, that is around the edge, and engulfs the whole button on hover!!!

## Day 5
Ugh more CSS, it sucks! Finally got a navbar that looks acceptable though thankfully, only took me like 3 hours :( otherwise minimal progress made.

Started creating sidebar
