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


def todo_anahtari_olustur(index, body, title):
   
    birlesik_metin = f"{index}-{body}-{title}"
    return hashlib.sha256(birlesik_metin.encode()).hexdigest()[:8]

dosya_yolu = 'db_files/data.json'
def metni_temizle(metin):
    return metin.replace("content=", "").replace("\n\n", "").replace("\n", "")

def listener(event):
    data_changed=event.path
    if data_changed!="/": 
    # write a file and increase a number on the first line
        with open('data_changed.txt', 'r') as f:
            sayi = int(f.read())
        sayi += 1
        with open('data_changed.txt', 'w') as f:
            f.write(str(sayi))
        print("Data changed:", data_changed)
db.reference('/').listen(listener)



