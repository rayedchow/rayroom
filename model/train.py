import torch, detectron2
from detectron2.utils.logger import setup_logger
setup_logger()

import os

# importing detectron2 utilities
from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.config import get_cfg

SEED = 9
THRESHOLD = 0.3

# Creating coco instances
from detectron2.data.datasets import register_coco_instances

for d in ["train", "valid", "test"]:
    register_coco_instances(f"bone_fractures_{d}", {},
                            f"./data/{d}_annotations.coco.json",
                            f"./data/{d}/images")

from detectron2.engine import DefaultTrainer

# constants for training detectron2 model
EPOCHS = 1800
NUM_CLASSES = 8
BASE_LR = 0.001

# Detectron2 Configuration
cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-Detection/faster_rcnn_X_101_32x8d_FPN_3x.yaml")) # RCNN COCO engine
cfg.DATASETS.TRAIN = ("bone_fractures_train",)
cfg.DATASETS.TEST = ()
cfg.DATALOADER.NUM_WORKERS = 0  # Set number of workers to 0
cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-Detection/faster_rcnn_X_101_32x8d_FPN_3x.yaml")
cfg.SOLVER.IMS_PER_BATCH = 2
cfg.SOLVER.BASE_LR = BASE_LR  
cfg.SOLVER.MAX_ITER = EPOCHS    
cfg.MODEL.ROI_HEADS.BATCH_SIZE_PER_IMAGE = 512  
cfg.MODEL.ROI_HEADS.NUM_CLASSES = NUM_CLASSES

# Needed this for the CPU
cfg.MODEL.DEVICE = "cpu"  # Ensure the model runs on the CPU

os.makedirs(cfg.OUTPUT_DIR, exist_ok=True)

# training model with CPU engine - RCNN
trainer = DefaultTrainer(cfg) 
trainer.resume_or_load(resume=False)
trainer.train()

# Saving the model
cfg.MODEL.WEIGHTS = os.path.join(cfg.OUTPUT_DIR, "model_final.pth")  
cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = THRESHOLD

# Saving Configuration with Pickle
import pickle
with open("cfg.pkl", "wb") as f:
    pickle.dump(cfg, f)