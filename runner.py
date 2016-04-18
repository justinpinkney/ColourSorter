import random
import subprocess
import os

options = {
	'--sort': ('R', 'G', 'B', 'Hue', 'Saturation', 'Brightness', 'Shuffle'),
	'--random': (0, 0.01, 0.05, 0.1),
	# '--distance': ('RGB', 'HSB'),
	'--preset': ('Centre', 'Corner', 'Border', 'Diagonal', 'Random'),
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


n_runs = 10

for run in range(n_runs):
	# Pick random options
	selected_options = {}
	for key in options.keys():
		selected_options[key] = random.choice(options[key])

	# Pick a random file
	selected_file = file_directory + random.choice(files)

	# Pick a new filename
	out_file = str(run) + ".png"

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
	# Run the colour sorter
	print(run_command)
	os.chdir(java_folder)
	
	out = subprocess.check_output(run_command)
	print(out)