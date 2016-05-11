import requests
import shutil
import random
import string

def save():

	url = 'https://source.unsplash.com/random/1920x1080'
	save_name = 'temp_' + ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(8)) + ".jpg"

	r = requests.get(url, stream=True)
	if r.status_code == 200:
		print('Got image')
		with open(save_name, 'wb') as f:
			shutil.copyfileobj(r.raw, f)
		return save_name
	else:
		print('Failed to get image')
		return None

if __name__ == '__main__':
	save()