from flask import Flask, request
from flask_cors import CORS, cross_origin
from model import fracture_detection

app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

@app.route("/fracture_model", methods=['POST'])
@cross_origin()
def fracture_model():
    data = request.get_json()
    predictionData = fracture_detection(data)
    return predictionData
