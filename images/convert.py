import cv2
import os

for i, filename in enumerate(os.listdir()):
	if '.png' in filename:
		image = cv2.imread(filename)
		cv2.imwrite(str(i) + '.jpg', image)