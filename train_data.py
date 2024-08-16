import pandas as pd
from sklearn.cluster import KMeans
from sklearn.tree import DecisionTreeClassifier
import pickle

class ModelTrainer:
    def __init__(self, input_file, model_file, clustered_file, feature_names_file):
        self.input_file = input_file
        self.model_file = model_file
        self.clustered_file = clustered_file
        self.feature_names_file = feature_names_file

    def load_data(self):
        self.data = pd.read_csv(self.input_file)

    def cluster_data(self):
        features = self.data.drop('Class', axis=1)
        kmeans = KMeans(n_clusters=2, random_state=42)
        self.data['cluster'] = kmeans.fit_predict(features)

    def save_clustered_data(self):
        self.data.to_csv(self.clustered_file, index=False)

    def train_model(self):
        X = self.data.drop(['Class', 'cluster'], axis=1)
        y = self.data['Class']
        clf = DecisionTreeClassifier(random_state=42)
        clf.fit(X, y)
        self.model = clf
        self.feature_names = X.columns.tolist()

    def save_model_and_features(self):
        with open(self.model_file, 'wb') as file:
            pickle.dump(self.model, file)
        with open(self.feature_names_file, 'wb') as file:
            pickle.dump(self.feature_names, file)

    def run(self):
        self.load_data()
        self.cluster_data()
        self.save_clustered_data()
        self.train_model()
        self.save_model_and_features()

if __name__ == "__main__":
    trainer = ModelTrainer(input_file=r'C:\Users\beyza\Desktop\training_project\Raisin_Dataset.csv', 
                           model_file='decision_tree_model.pkl', 
                           clustered_file=r'C:\Users\beyza\Desktop\training_project\clustered_dataset.csv',
                           feature_names_file='feature_names.pkl')
    trainer.run()
