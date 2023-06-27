
from flask import Flask, request, jsonify
import secrets
from integrityManager import IntegrityManager

app = Flask(__name__)
app.config['SECRET_KEY'] = secrets.token_urlsafe(128)

# Initiaizing IntegrityManager
PACKAGE_NAME = "com.smleenull.play_integrity_api"
ALLOWED_WINDOW_MILLIS = 30 * 1000 # 30 seconds * 1000 = 30000 milliseconds
integrityManager = IntegrityManager(PACKAGE_NAME, ALLOWED_WINDOW_MILLIS)

# Setting up the Server IP on the client for connection testing purpose
@app.route("/hello", methods=["GET"])
def serverConnectionCheck():
    return jsonify({'connection': True})

@app.route("/a/integrity/fetch/random-value", methods=["GET"])
def generateRandomValue():
    return jsonify({'random': integrityManager.generateRandomValue()})

@app.route("/a/integrity/verify/integrity-token", methods=["POST"])
def verifyIntegrityToken():
    requestBody = request.get_json()
    if not requestBody:
        return jsonify({"message": "No data provided"}), 400
    
    integrityToken = requestBody.get("integrityToken")
    if not integrityToken:
        return jsonify({"message": "No integrityToken provided"}), 400

    integrityPayload = integrityManager.integrityVerification(integrityToken, PACKAGE_NAME)

    return jsonify({"message": integrityPayload}), 200



if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8888)