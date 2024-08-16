1. Read a dataset line by line using Java with SparkContext and RDD. When a specified parameter is found, print an alert message to the console. You should find a suitable dataset from UCI (for example, the Wine dataset) that is appropriate for clustering.

2. Using Python with scikit-learn, perform a simple clustering of this dataset into 3 clusters.

3. For the clustered data, create a new training set by adding a new column with the cluster names alongside the data points.

4. Train a decision tree on this data using scikit-learn.

5. Save the trained model as a pickle file.

6. Create a REST service in Python using Flask (with flask-cors) that will take a similar dataset and pass it through the Java application using RDDs. For each tuple, predict the "class" of the data point using the REST service in Python, and if a certain class (pattern) is found, trigger an alert in the Java application.



Question 1

A Spark configuration has been set up, and a SparkContext running in local[*] mode has been created. The data.csv file is read, with the first row treated as the header and removed from the dataset.
To search for the specified parameter and print an "alert" message to the console:


String searchParam = "IndiGo"; // Parameter to search for
dataLines.filter(line -> line.contains(searchParam))
         .foreach(line -> System.out.println("alert: " + line));

         
Question 2

In this question, the classes training_data.py, app.py, and PredictionApp.java have been created. The dataset chosen is the Raisin_Dataset.
Using Python, KMeans clustering has been performed on the data (with 3 clusters). The clustering results were added to the dataset and saved as clustered_dataset.csv. The trained model and feature names were stored in pickle format (decision_tree_model.pkl and feature_names.pkl).

ModelTrainer Class

This class handles the clustering of the data and the training of the decision tree model. The purpose of the code is to cluster the data using KMeans based on the features in the dataset and then train a decision tree model using this data.

App.py Class

This class creates a RESTful service using Flask and Flask-CORS. The service makes predictions using a trained model and responds to incoming requests.

def load_model_and_features(self): This function loads the saved model and feature names from files in pickle format.
def predict(self, features): This function makes predictions using the model with the given list of features. The features are converted to a DataFrame, and the column names are matched with the feature names expected by the model. The model then makes a prediction, and the result is returned.
python
Copy code
@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json(force=True)
    features = data['features']  # Expecting 'features' to be a list of feature values
    prediction = service.predict(features)
    return jsonify({'prediction': prediction})
The /predict endpoint is defined to handle POST requests. Data in JSON format is received from the request and extracted as a features list. A prediction is made using the PredictionService, and the result is returned in JSON format.

Post Successful Output:
127.0.0.1 - - [01/Aug/2024 16:37:52] "POST /predict HTTP/1.1" 200 -

App.java Class

When the application starts, the model and feature names are loaded from files. When a POST request is made to the /predict endpoint, the model makes a prediction using the features from the request. The result is then returned in JSON format.

Throughout this task, I encountered many 500 errors, which took up a lot of time. The data was altered, and characters were not being processed correctly. One of the errors I faced was, "Error during prediction: Expected 13 features, but got 14." Later, I encountered errors like "Error during prediction: 2030 columns passed, passed data had 10 columns."

PredictionApp.java

This class reads the data file (Raisin_Dataset.csv), extracts features for each data row, and gets predictions from the Python-based REST service. If the prediction is "Kecimen," an alert is triggered.

The given data row is split by commas, and the first 7 columns are returned as a list of Doubles. These columns are the features that the model will use.
Then, the features are converted into JSON format.
A POST request is sent to http://localhost:5000/predict (the Python-based REST service), and a prediction is received. The response is processed as JSON, and the prediction result is returned. If the prediction is "Kecimen," an alert is triggered, and this data row is printed to the console.
Output (First 3 examples copied):


Alarm! Record:
87524,442.2460114,253.291155,0.819738392,90546,0.758650579,1184.04,Kecimen

Alarm! Record:
75166,406.690687,243.0324363,0.801805234,78789,0.68412957,1121.786,Kecimen

Alarm! Record:
90856,442.2670483,266.3283177,0.798353619,93717,0.637612812,1208.575,Kecimen







