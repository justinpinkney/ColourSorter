import random
import subprocess
import os
from multiprocessing import Process, JoinableQueue
import string
import unsplash

def worker(queue):
	print("Starting worker")
	while True:
		command = queue.get()
		print(command)
		subprocess.run(command)
		queue.task_done()
	

def generate_command():
	options = {
		'--sort': ('R', 'G', 'B', 'Hue', 'Saturation', 'Brightness', 'Shuffle'),
		'--reverse': ('True', 'False'),
		'--random': (0, 0, 0, 0.01, 0.05, 0.1),
		'--distance': ('RGB', 'HSB'),
		'--preset': ('Centre', 'Corner', 'Border', 'Diagonal', 'Random', 'Edge', 'RandomLine'),
	}

	resolutionX = 1920
	resolutionY = 1080

	unsplash = True

	if unsplash:
		selected_file = unsplash.save()
	else:
		
		file_directory = "C:\\Users\\Justin\\Pictures\\fancy photos\\portfolio\\"

		all_files = os.listdir(file_directory)

		file_types = ["jpg", "png", "tiff"]
		files = []
		for dirpath, dirnames, filenames in os.walk(file_directory):
			for name in filenames:
				this_file = os.path.join(dirpath, name)
				if any(ext in this_file.lower() for ext in file_types):
					files.append(this_file)
		# Pick a random file
		selected_file = random.choice(files)

	# Pick random options
	selected_options = {}
	for key in options.keys():
		selected_options[key] = random.choice(options[key])

	

	# Pick a new filename
	out_file = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(8)) + ".png"

	# Assemble the string
	run_command = ["java",
					"com.cutsquash.coloursorter.ColourSorter", 
					str(resolutionX),
					str(resolutionY),
					selected_file,
					"--output",
					out_file]
				
	# Add the options
	for key in selected_options.keys():
		run_command.append(key)
		run_command.append(str(selected_options[key]))

	# Save settings to a text file
	with open("record.txt", 'a') as record_file:
		record_file.write(out_file)
		record_file.write('\t')
		for item in run_command:
			record_file.write(item + ' ')
		record_file.write('\n')

	return run_command
		


if __name__ == '__main__':
	
	away = True

	if away:
		java_folder = "C:\\Software\\GitHub\\ColourSorter\\out\\production\\ColourSorter"
	else:
		java_folder = "C:\\Users\\Justin\\Documents\\Development\\Github\\ColourSorter\\out\\production\\ColourSorter"

	os.chdir(java_folder)
	n_runs = 5
	n_workers = 4

	q = JoinableQueue()

	for i in range(4):
		print("Starting process %d" % i)
		p = Process(target=worker, args=(q,))
		p.daemon = True
		p.start()

	for run in range(n_runs):
		new_command = generate_command()
		print(new_command)
		q.put(new_command)

	q.join()