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

## Day 6
A bit more styling added to the sidebar, man i hate CSS, hopefully i can be done with it soon, im so much better at coding logic haha, added the a trending up/down icon for the trending beaches sidebar and made it align nicely with nice gaps, and a nice green colour.

## Day 7 
Side bar done for now, wow that took a long time holy, just gotta do the background and maybe spruce up the nabvbar, then its onto the actual fake logic.

Adjusted the length of the search bar so it looks right.

Added a really cool typewriter animation to the centre header using CSS but mostly useEffect which i had never used before, took ages, but so worth it!!

Learnt shorthand if statements, they are so useful!!

Turned the header into this super cool wave animation, using svgs and starting each wave with a delay so they are offset!

## Day 8
I've switched up the navbar and the account buttons to glassmorphism and i do think it looks better, but idk why my website always looks so tacky its infuriating. I just wanna get back to coding logic i hate CSS and design.

Small change to the shape of the account buttons defo looks better.

Decided to give up on the appeareance for now as its driving me insane, ill come back to it later.

Just set up react router, and changes my css, so the logo navbar and account buttons persist across every page and damn it looks so cool, and holy is it so useful too be able to do that!!

Added a single background photo, just as a test, gonna leave it like this for now, work in progress.

## Day 9
I've completely redesigned the look and i think personally that it is way better, made the sidebar glasmorphism along with the search bar!!

Completely switched up the colours in the top header and it looks way better!!! 

Next goal is to find someway to blend that header line into my background.

Close to sorting out the positioning of seachbar and center header.

Getting started on generic beach pages.

## Day 10
I've begun really working on the beach pages, have the header working just working on getting a per beach rating.

Every beach now has as a fake rating doing this as if i recieved a real array from a database.

Every beach page now has the css for the stats box setup.
