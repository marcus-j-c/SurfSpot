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

## Day 11
Technically gone backwards bc i have removed my hardcoded data and instead have started creating a fake backend json as this will make my next stage easier, linking up the back and front end. So it will be worth it in the end as it means i will end up deleting less code.

Added a nice glow animation to my stats table on the beach pages also, did this before purging the fake data and hence the beach pages no longer work as the fake backend is still a work in progress.

Made it so only the homepage doesnt scroll every other page can, this is useful bc i need my beach pages to scroll.

## Day 12
Back to where i was before but have reached it using db.json, and hence this will make connecting up the backend way easier.

Now added fake beach stats to db.json and have also passed them as props to beach stats, everything is coming together just gonna fix the CSS styling, then work on the rest of the beach page.

Scrollbar hidden.


## Day 13
Stats box fully styled moving onto the why the rating has been given.

Why rating added and fully styled.

Fake good stuff all added and styled to be honest the styling is a lot of copy and pasting of my previous styling at this point to keep things consistent which is nice.

Fake bad stuff all added and styled to be honest the styling is a lot of copy and pasting of my previous styling at this point to keep things consistent which is nice.

## Day 14
Search bar fully working, can now search for one of the 10 fake beaches and it will take you to the corresponding beach page.

Fixed the beach page overflow issue.

Culled some unused old code, like forecast card, and typed text.

Figured out how to extend the background blur into bottom of the page padding!

Big progress, fake frontend is done, now i can fully connect up the front and back end!!

Move db.json stuff over into the beachcontroller in my backend, ran bruno tests and everything worked beautifully, guess i just gotta connect react up to the backend now!

FRONT AND BACKEND ALL CONNECTED THAT WAS ACTUALLY SO EASY I CANT BELIEVE ITS ACTUALLY CONNECTED!!!

## Day 15
Big progress so far, have got my first api requests hopefully working via my coords request method, using open meteo and then if that doesnt work, like open meteo doesnt seem to be able find lots of beaches like bonzai pipeline but you can make way more requests per second, which is why nominatim, which seems to be able to find all these beaches is my fallback.

Now ive got the coords ive begun adding the method, to get the beach data, and ive already added if i couldnt find the coords, just return unknown beach.

Huge progress, am getting real data for beaches and its working beautifully all i have to do now, is convert weathercode and wind direction into their respective strings, like ENE or mostly sunny!!
 
Then i can move onto the rating system and the analysis of said conditions and rating.

## Day 16
Created maps to convert weathercode and wind direction into their respective strings, and also made it so that safe double returns a -1 and if that -1 is read, then it knows to return unknown beach.

Added logging

Added stripping of suffixes, bc i tried teahupo'o-beach in bruno and it gave me unknown beach but i knew teahupo'o worked, so stripping suffixes if both api's fail.

Changed a tiny thing in the CSS of the stats box so the text is always on one line and is always the same size.
