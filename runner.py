import random
import subprocess
import os

options = {
	'--sort': ('R', 'G', 'B', 'Hue', 'Saturation', 'Brightness', 'Shuffle'),
	# '--random': (0, 0.05, 0.1, 0.2, 0.4, 0.8),
	# '--distance': ('RGB', 'HSB'),
	# '--preset': ('Centre', 'Corner', 'Border', 'Diagonal', 'Random'),
}

resolutionX = 200
resolutionY = 200

file_directory = "C:\\Users\\Justin\\Documents\\Development\\Github\\ColourSorter\\data\\"
files = ("im (5).JPG", "saved.png")

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
			record_file.write(item)
		record_file.write('\n')
	# Run the colour sorter
	print(run_command)
	os.chdir("C:\\Users\\Justin\\Documents\\Development\\Github\\ColourSorter\\out\\production\\ColourSorter")
	
	out = subprocess.check_output(run_command)
	print(out)