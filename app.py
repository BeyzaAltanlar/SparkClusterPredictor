from flask import Flask, request, jsonify
from flask_cors import CORS
import pickle
import pandas as pd

class PredictionService:
    def __init__(self, model_file, feature_names_file):
        self.model_file = model_file
        self.feature_names_file = feature_names_file
        self.load_model_and_features()

    def load_model_and_features(self):
        with open(self.model_file, 'rb') as file:
            self.model = pickle.load(file)
        with open(self.feature_names_file, 'rb') as file:
            self.feature_names = pickle.load(file)

    def predict(self, features):
        df = pd.DataFrame([features], columns=self.feature_names)
        prediction = self.model.predict(df)
        return prediction[0]

# Flask app setup
app = Flask(__name__)
CORS(app)
service = PredictionService(model_file='decision_tree_model.pkl', feature_names_file='feature_names.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json(force=True)
    features = data['features']  # Expecting 'features' to be a list of feature values
    prediction = service.predict(features)
    return jsonify({'prediction': prediction})

if __name__ == '__main__':
    app.run(debug=True)
