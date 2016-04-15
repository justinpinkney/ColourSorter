options = {
	'sort': ('R', 'G', 'B', 'Hue', 'Saturation', 'Brightness', 'Shuffle')
	'random': range(10)/10.0
	'distance': ('RGB', 'HSB')
	'preset': ('Centre', 'Corner', 'Border', 'Diagonal', 'Random')
}

resolution = (1920, 1080)

file_directory = 'root_directory'
files = ('im1.jpg', 'etc')

# Pick random options
for key in options.keys():
	selected_options[key] = random.choice(options[key])
# Pick a random file
# Pick a new filename, and save settings to a text file
# Run the colour sorter