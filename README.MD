<h1 align="center" style="font-weight: normal;">tr<b>AI</b>ding-cards</h1>
<p align="center">An AI generated trading card game.</p>
<p align="center"><a href="">This project was part of the 2023 Digital Science Night</a></p>
<p align="center">DiffusionBooth | tr<b>AI</b>ding-cards | <a href="https://github.com/okafke/trAIding-cards-gallery">tr<b>AI</b>ding-cards-gallery</a></p>

<p style="text-align: center;">
Are 151 Pokémon not enough for you?
Ever wanted to see what a hungry fridge on fire looks like?
A demonic post box?
Or the Kitchen Mixer Overlord? 
Let your imagination run wild: tr<b>AI</b>ding-cards uses Dall-E and ChatGPT to create cards for you.
</p>

<p align="center">
    <img src="docs/30e58b0f-581a-4b0a-9882-e96f4748d196-card.png" alt="Image 1" width="200px"/>
    <img src="docs/bdf3bbb5-9954-4707-8b24-6f23d830bccc-card.png" alt="Image 4" width="200px"/>
    <img src="docs/75f2569e-c54a-4e80-8131-51189901c996-card.png" alt="Image 3" width="200px"/>
</p>

<p align="center">
    <img src="docs/1ed73ae7-d02c-44e9-8a14-534af9dd952e-card.png" alt="Image 2" width="200px"/>
    <img src="docs/13898eb7-c8e0-42d4-bfac-5c3398da3f45-card.png" alt="Image 5" width="200px"/>
    <img src="docs/7de6da01-7386-4419-aa9d-23d838f4fba2-card.png" alt="Image 6" width="200px"/>
</p>

## Setup
> Note that lots of the code in this repository is still written with the Digitial Science Night in mind.
> E.g. that includes lots of the specific printer code and printer ips.

Clone the repository. Create a file called `env.properties` in [src/main/resources](src/main/resources) and add your
OpenAI API key:
```properties
OPEN_AI_API_KEY=<your OpenAI key>
```
Then execute:
```
./gradlew bootRun
```
Visit [http://localhost:8080](http://localhost:8080).
Start creating cards :)
