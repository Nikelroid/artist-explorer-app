from flask import Flask, request, jsonify
from flask import Flask, url_for
from flask import render_template,request
from flask import Flask, request, jsonify
from google import genai

client = genai.Client(api_key="AIzaSyCJZdB3Oa4uK9Esig344UvYtxU_ZrrczK4")


app = Flask(__name__)
@app.route('/', methods=['GET'])
def index():
    client.models.generate_content(
    model="gemini-2.0-flash", contents="You are a helpful assistant for students. You are demo assistant for ai alternative of assigment 2 for next semester in the CSCI571 Web Technologies course. ",
    )
    return render_template('index.html')

@app.route('/api/chat/<message>', methods=['GET'])
def chat(message):
    user_message = message
    if not user_message:
        return jsonify({'error': 'No message provided'}), 400

    try:



        
        response = client.models.generate_content(
            model="gemini-2.0-flash", contents= user_message,
        )
        
        bot_response = response.text
        bot_response = bot_response.replace('\n', '').replace('*', '').replace('\\', '').replace('"', '')
        print(bot_response)
        return jsonify({'response': bot_response})
    except Exception as e:
        raise e
        # Handle any errors that occur during the API
        return jsonify({'error': str(e)}), 500
if __name__ == '__main__':
    app.run(host="127.0.0.1", port=8080, debug=True)