from fastapi import FastAPI

from .routes import router


def initialize_application() -> FastAPI:
    app = FastAPI()
    app.include_router(router)
    return app
