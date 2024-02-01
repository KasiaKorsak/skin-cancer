import io

from PIL import Image
from fastapi import APIRouter, File
from predictions.model import SkinCancerPredictionService

router = APIRouter()


@router.post("/predict")
def predict(image_bytes: bytes = File()):  # mediatype - multipart/formdata
    image = Image.open(io.BytesIO(image_bytes))
    image = image.resize((224, 224))
    return {
        "typ": SkinCancerPredictionService().predict(image)
    }
