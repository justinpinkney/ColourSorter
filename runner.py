import random
import subprocess
import os
import multiprocessing
import string

def worker(command):
	print("Running: ")
	print(command)
	subprocess.run(command)
	return

def generate_command():
	options = {
		'--sort': ('R', 'G', 'B', 'Hue', 'Saturation', 'Brightness', 'Shuffle'),
		'--reverse': ('True', 'False'),
		'--random': (0, 0.01, 0.05, 0.1),
		'--distance': ('RGB', 'HSB'),
		'--preset': ('Centre', 'Corner', 'Border', 'Diagonal', 'Random', 'Edge', 'RandomLine'),
	}

	resolutionX = 1920
	resolutionY = 1080

	away = False

	if away:
		java_folder = "C:\\Software\\GitHub\\ColourSorter\\out\\production\\ColourSorter"
		file_directory = "C:\\Software\\GitHub\\ColourSorter\\data\\"
	else:
		java_folder = "C:\\Users\\Justin\\Documents\\Development\\Github\\ColourSorter\\out\\production\\ColourSorter"
		file_directory = "C:\\Users\\Justin\\Pictures\\fancy photos\\portfolio\\"

	all_files = os.listdir(file_directory)
	file_types = ["jpg", "png", "tiff"]
	files = []
	for this_file in all_files:
		if any(ext in this_file.lower() for ext in file_types):
			files.append(this_file)

	# Pick random options
	selected_options = {}
	for key in options.keys():
		selected_options[key] = random.choice(options[key])

	# Pick a random file
	selected_file = file_directory + random.choice(files)

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

	# # Save settings to a text file
	# with open("record.txt", 'a') as record_file:
	# 	record_file.write(out_file)
	# 	record_file.write('\t')
	# 	for item in run_command:
	# 		record_file.write(item + ' ')
	# 	record_file.write('\n')

	return run_command
		


if __name__ == '__main__':
	
	java_folder = "C:\\Users\\Justin\\Documents\\Development\\Github\\ColourSorter\\out\\production\\ColourSorter"
	n_runs = 100

	for run in range(n_runs):
		# Run the colour sorter
		os.chdir(java_folder)
		
		jobs = []
		for i in range(4):
			p = multiprocessing.Process(target=worker, args=(generate_command(),))
			jobs.append(p)
			p.start()
			print("starting" + str(i))
		
		for p in jobs:
			p.join()
