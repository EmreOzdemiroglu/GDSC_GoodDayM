# GSC
Google Solution Challenge 2024

App Name: GooDaym App

# About Project

In this project, our goal is based on the principle of an artificial intelligence analyzing health protocols related to the todo input by the user and generating new, personalized todos accordingly. Our aim is if there are healthier and more efficient ways of completing tasks that individuals undertake, and if these methods are academically supported, to synthesize this research through artificial intelligence and suggest todos to the individual. Of course, due to copyright reasons, we wrote the protocols ourselves in the application. However, according to the results of our own tests, the application is very open to future developments. Unfortunately, due to insufficient time allocated to the project, there is a significant gap between our ideas and what we have accomplished.

## Future Improvements

### Idea Based
- [ ] Train and fine-tune language models like [BioMistral](https://huggingface.co/BioMistral/BioMistral-7B), to customize our project's perspective on protocols.
- [ ] Developing a rag system that separates protocols and uses open local models for being open-source friendly. For example: [Nomic-embed-text](https://huggingface.co/nomic-ai/nomic-embed-text-v1)
- [ ] Solving the compatibility issues with the [Autogen](https://github.com/microsoft/autogen) library and Gemini, and modifying the agent schema to use different network structures both in rag and the main app part is necessary.
- [ ] Adding a note application base, thus enabling the AI to analyze notes and create appropriate todos blended with protocols. (Somewhat like a journal)
- [ ] I believe open-source projects like [Logseq](https://github.com/logseq/logseq) and [PrivateGPT](https://github.com/imartinez/privateGPT) could assist in developing this project. Especially, a search system integrated with autogen for protocol production and rag structure for enthusiasts of the linked-base structure, along with PrivateGPT, would make the application incredibly versatile.
- [ ] Looking from a broader perspective, the following features could be added. Incorporating a community section within the app would allow for the visibility of different thought networks (networks formed by individuals based on their own studies), enabling people to better understand each other [Logseq]; seeing different protocol implementations and their results would allow individuals to choose protocols based on suitability, plus indexing health studies with embeds and easing analysis with the note section will be very beneficial in creating a thought network. Keeping all developed technologies open source will be a significant advantage for the project's development.

## App Based
- [ ] Making the interface more user-friendly, improving the authentication system.
- [ ] Developing web and Linux/Mac/Windows compatible app versions with electronjs.

### Warning:
- This project was prepared for Google Solution Challenge 2024.
- This app is in the demo phase and uses Generative AI.
- For this reason, some unexpected errors may occur, please pay attention to this.
- Our purpose is only sharing the idea behind this project. It can be improved by a lot.

## Build With
- Android
- Kotlin
- Jetpack Compose
- Python
- Gemini AI
- LangChain
- AWS
- Firebase
- Realtime Database

# Prerequisites

## For APK
- Android 14 and above must be installed on your device.
- Don't forget to grant the necessary permissions for APK installation.
  
## For Android Studio and FireBase
- Make sure that your Android Studio is up to date and that the project you open is compatible with JetPack Compose.
- After adding the Kotlin files, make the necessary imports.
- Don't forget to open your own Firebase application and import it into Android Studio. For data security reasons, do not share your database with anyone.
- Don't forget to open Realtime Database in Firebase


## Server-side

### Setting Up Your Environment

1. **Virtual Environment:** We recommend using a virtual environment for this project to manage dependencies efficiently. You can choose any virtual environment tool you prefer (e.g., `virtualenv`, `conda`).

2. **Installing Dependencies:** Once your virtual environment is activated, install the necessary packages using the `requirements.txt` file:
   ```
   pip install -r requirements.txt
   ```
3. **API keys and URLs for RT DB:** You have to put your firebase_db url and gemini_api key in the main and number_upper app.
### Running the Application

To ensure the application runs smoothly, you need to launch two separate instances in the following order:

1. **Number Upper:** Start the first instance to run the `number_upper.py` script. This service will act as a listener, processing incoming data.

2. **Main Application:** After the first service is up, launch the second instance to run the `main.py` script. This will also act as a listener and is essential for the core functionality of the application.

### Using the Application

- **Adding a Todo:** With both services running, you're all set! Try adding a new todo item in the application. Based on the input and predefined protocols, the AI will then generate a new todo item, enhancing your list with intelligent suggestions.

### Utilizing Raspberry Pi Zero

- **Raspberry Pi Zero Integration:** I've utilized a Raspberry Pi Zero, which I had at home, to demonstrate the project's flexibility and compatibility with low-cost hardware platforms. This showcases the application's ability to run on various devices, making it accessible for a wide range of users.

# Usage
- First of all, when the application opens, you will be greeted by a "Sign Up" screen.
- You can enter the application by entering the required information here.
  
![Ekran görüntüsü 2024-02-21 162359](https://github.com/EmreOzdemiroglu/GDSC/assets/153070257/4ded8e86-5a7e-493a-8c10-e70f65d47c87)

- If you have logged in before, you can log in by pressing the login button.

![Ekran görüntüsü 2024-02-21 162918](https://github.com/EmreOzdemiroglu/GDSC/assets/153070257/a421f6c5-4e50-4382-889f-274929048fb6)

You will then see 3 different buttons on the main application screen:
- To-Do List
- Protocols
- Notes Page (To be added after the demo)

![Ekran görüntüsü 2024-02-21 163248](https://github.com/EmreOzdemiroglu/GDSC/assets/153070257/0646d7ba-5a90-48a2-bb16-91356869e36a)

- You can enter your To-Do by pressing the "Add Task" button.
- You can then wait for the To-Do that the AI will create for you.

![Ekran görüntüsü 2024-02-21 182356](https://github.com/EmreOzdemiroglu/GDSC/assets/153070257/fc37b0b1-0b3b-402a-86cf-d93d3aae2286)

- Finally, you can access all the recommended information from the 2nd tab.
- Notes section in the 3rd tab will be added after the demo version.

![Ekran görüntüsü 2024-02-21 183339](https://github.com/EmreOzdemiroglu/GDSC/assets/153070257/48897719-cb4d-4939-9f80-821a2614e918)













