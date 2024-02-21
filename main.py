import json
import time
import hashlib
import firebase_admin
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.prompts.prompt import PromptTemplate
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('')
print("cred:",cred)
firebase_admin.initialize_app(cred, {
    'databaseURL': ""
})

default_llm = ChatGoogleGenerativeAI(
    model="gemini-pro",
    verbose=True,
    temperature=0.1,
    google_api_key="",
    )


with open('protocol.json', 'r') as file:
    protocols = json.load(file)

decider_prompt = PromptTemplate.from_template("You are a neuroscientist. {Answer_This}")

todo_creator_prompt = PromptTemplate.from_template("You are a psychiatrist who graduated from Harvard, and you are also a writer. Your mission: {Answer_This}")

critic_prompt = PromptTemplate.from_template("You are an incredibly respected and intelligent critic who graduated from Oxford. You have a master's degree in literature. You are very talented. You can explain in a few words beautifully. Task: {Answer_This}")

editor_prompt = PromptTemplate.from_template("You are an incredibly respected editor who graduated from Oxford, meticulously choosing each word and always using the correct ones. You must implement the given task. Task: {Answer_This}")


decider_chain= decider_prompt | default_llm 
todo_chain = todo_creator_prompt | default_llm
critic_chain = critic_prompt | default_llm 
editor_chain = editor_prompt | default_llm 


def todo_anahtari_olustur(index, body, title):
   
    birlesik_metin = f"{index}-{body}-{title}"
    return hashlib.sha256(birlesik_metin.encode()).hexdigest()[:8]

dosya_yolu = 'db_files/data.json'
def metni_temizle(metin):
    return metin.replace("content=", "").replace("\n\n", "").replace("\n", "")

def listener(event):
    
    data_changed=event.path
    if data_changed!="/": 
        ref = db.reference('/')
        with open(dosya_yolu, 'w') as f:
            json.dump(ref.get(), f, indent=4)
        with open(dosya_yolu, 'r') as dosya:
            todos = json.load(dosya)
        print("Dosya okundu")
        for kullanici, bilgiler in todos['Users'].items():
            if 'ToDos' in bilgiler:
                todo_sayisi = len(bilgiler['ToDos'])
                print(todo_sayisi)
                yeni_todo_listesi = {}  
                print("kullanıcı",kullanici)
                
                for index, (todo_id, todo_bilgisi) in enumerate(bilgiler['ToDos'].items(), start=1):
                    if isinstance(todo_bilgisi, dict) and todo_bilgisi.get('linked_todo', '') == "" and todo_bilgisi.get('AIGenerated', '') == 'false':
                            yeni_anahtar = todo_anahtari_olustur(index, todo_bilgisi.get('body', ''), todo_bilgisi.get('title', ''))
                            invoked_chain_decider = decider_chain.invoke({"Answer_This": f"I will give you 2 different texts. You only need to write the protocol. ONLY THE PROTOCOL! In one, there are a person's to-dos, and in the other, synthesized 'protocols' named different health recommendations from academic health studies. Your task is to determine which protocol would be suitable for the person based on the to-dos. The to-dos are {todos}, and now the protocols {protocols}, now recommend a protocol based on this information. NOTE EXTREMELY IMPORTANT! THE PROTOCOL SHOULD BE PRESENTED BY EXTRACTING FROM THE MAIN POINT SECTION."})
                            print("Decider :" + str(invoked_chain_decider))
                            invoked_chain_todo = todo_chain.invoke({"Answer_This": f"Your goal is to create a to-do item from the text given to you. You only need to write the to-do. Text: {invoked_chain_decider}"})
                            print("Todo :" + str(invoked_chain_todo))
                            invoked_chain_critic = critic_chain.invoke({"Answer_This": f"Here there is a to-do {invoked_chain_todo} and here there is a protocol {invoked_chain_decider} your goal is to see if these two match. You should give a one-word answer like Matches/Does Not Match. After that, you should create guidelines on what is needed to better write the to-do. This guideline should be detailed so that the next language model can act accordingly."})
                            print("Critic :" + str(invoked_chain_critic))
                            invoked_chain_editor = editor_chain.invoke({"Answer_This": f"Your goal is to read the writings given to you under the name of critique and then change the to-do given to you according to the appropriateness based on the critiques or leave it as is. You need to write one sentence. For the to-do. To-do: {invoked_chain_todo} Critique:{invoked_chain_critic}"})
                            print("Editor :" + str(invoked_chain_editor))
                            invoked_chain_todo_title = todo_chain.invoke({"Answer_This": f"Your goal is to find a 4-5 word title for this writing. {invoked_chain_editor}"})
                            print("Todo Title :" + str(invoked_chain_todo_title))
                            print("todoid",todo_id) 
                            print("index",index)
                            chain_todo_title_cl = metni_temizle(str(invoked_chain_todo_title))
                            todos['Users'][kullanici]['ToDos'][todo_id]['linked_todo'] = f"{chain_todo_title_cl} Generated BY AI"
                            chain_editor_cl = metni_temizle(str(invoked_chain_editor))
                            yeni_todo = {
                                "body": f"{chain_editor_cl}",
                                "date": todo_bilgisi.get('date', ''),
                                "title": f"{chain_todo_title_cl} Generated BY AI",
                                "AIGenerated": "true",
                                "linked_todo": todo_bilgisi.get('title', '')
                            }
                            yeni_todo_listesi[yeni_anahtar] = yeni_todo 
                
                bilgiler['ToDos'].update(yeni_todo_listesi)

        with open(dosya_yolu, 'w') as dosya:
            json.dump(todos, dosya, indent=4, ensure_ascii=False)
        ref.update(todos)
db.reference('/').listen(listener)

def metni_temizle(metin):
    return metin.replace("content=", "").replace("\n\n", "").replace("\n", "")
