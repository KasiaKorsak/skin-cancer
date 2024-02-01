from pathlib import Path

import keras
import numpy as np
from PIL.Image import Image
from keras.models import load_model

#     nv = "Znamię melanocytowe"
#     mel = "Czerniak"
#     bkl = "Łagodne zmiany przypominające rogowacenie"
#     bcc = "Rak podstawnokomórkowy"
#     akiec = "Rogowacenie słoneczne"
#     vasc = "Zmiany naczyniowe"
#     df = "Włókniak"


class SkinCancerPredictionService:
    def __init__(self):
        model_path = (Path(__file__).parent / "model.h5")
        self.model: keras.Model = load_model(model_path)

    def predict(self, image: Image) -> str:
        arr = np.asarray(image)
        arr = np.expand_dims(arr, axis=0)  # https://datascience.stackexchange.com/questions/13461/how-can-i-get-prediction-for-only-one-instance-in-keras

        img_preprocessed = arr / 255.

        prediction = self.model.predict(img_preprocessed)[0].tolist()
        max_value = max(prediction)
        max_index = prediction.index(max_value)
        print(prediction)
        dx_list = ['mel', 'bcc', 'akiec', 'df', 'vasc', 'nv', 'bkl']
        dx = dx_list[max_index]
        print(dx)
        return dx
