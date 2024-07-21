# import random
import torch, detectron2

from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.config import get_cfg

import cv2
from detectron2.utils.visualizer import Visualizer
from detectron2.data import MetadataCatalog
from detectron2.utils.visualizer import ColorMode
import numpy as np
import base64
import io
from PIL import Image

# Creating coco instances
from detectron2.data.datasets import register_coco_instances

class_names = ['', 'elbow fracture', 'fingers fracture', 'forearm fracture', 'humerus fracture', 'humerus injury', 'shoulder fracture', 'wrist fracture']

for d in ["train", "valid", "test"]:
    register_coco_instances(f"bone_fractures_{d}", {},
                            f"./data/{d}_annotations.coco.json",
                            f"./data/{d}/images")
    MetadataCatalog.get(f"bone_fractures_{d}").thing_classes = class_names

trained_cfg = get_cfg()
trained_cfg.merge_from_file(model_zoo.get_config_file("COCO-Detection/faster_rcnn_X_101_32x8d_FPN_3x.yaml"))

trained_cfg.MODEL.ROI_HEADS.NUM_CLASSES = 8
trained_cfg.MODEL.ROI_HEADS.BATCH_SIZE_PER_IMAGE = 512
trained_cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.3
trained_cfg.MODEL.WEIGHTS = "./output/model_final.pth"
trained_cfg.MODEL.DEVICE = "cpu"

predictor = DefaultPredictor(trained_cfg)

def data_uri_to_cv2_img(uri):
    encoded_data = uri.split(',')[1]
    nparr = np.frombuffer(base64.b64decode(encoded_data), np.uint8)

    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    return img

# im = cv2.imread("./data/test/images/image2_953_png.rf.97a41890b4badde45bfc390f9f5c9d12.jpg")
# outputs = predictor(im)
# v = Visualizer(im[:, :, ::-1], metadata=MetadataCatalog.get('bone_fractures_train'), scale=0.9, instance_mode=ColorMode.IMAGE_BW)
# out = v.draw_instance_predictions(outputs["instances"].to("cpu"))
## cv2.imshow('skibidi', out.get_image()[:, :, ::-1])
# out.save("./predictions/test.png")
## cv2.waitKey(0)
## print(outputs)

def fracture_detection(data):
    img = data_uri_to_cv2_img(data["imgdata"])
    outputs = predictor(img)
    v = Visualizer(img[:, :, ::-1], metadata=MetadataCatalog.get('bone_fractures_train'), scale=1, instance_mode=ColorMode.IMAGE_BW)
    out = v.draw_instance_predictions(outputs["instances"].to("cpu"))
    
    img_bgr = out.get_image()
    img_rgb = img_bgr[:, :, ::-1]
    pil_image = Image.fromarray(img_rgb)
    buffered = io.BytesIO()
    pil_image.save(buffered, format="PNG")

    img_base64 = base64.b64encode(buffered.getvalue()).decode('utf-8')

    print({ "img": img_base64, "scores": outputs["instances"].get_fields()["scores"].tolist(), "classes": outputs["instances"].get_fields()["pred_classes"].tolist() })

    return {
        "img": img_base64,
        "scores": outputs["instances"].get_fields()["scores"].tolist(),
        "classes": outputs["instances"].get_fields()["pred_classes"].tolist()
    }