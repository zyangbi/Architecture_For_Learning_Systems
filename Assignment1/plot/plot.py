import matplotlib.pyplot as plt
import pandas as pd

# Read the CSV file into a DataFrame
# df = pd.read_csv("binary.csv", header=None, names=["Error"])
df = pd.read_csv("bipolar.csv", header=None, names=["Error"])
# df = pd.read_csv("binaryMomentum.csv", header=None, names=["Error"])
# df = pd.read_csv("bipolarMomentum.csv", header=None, names=["Error"])

fig, ax = plt.subplots()

# Plot the data
ax.plot(df["Error"])

# Setting the labels and title
ax.set_xlabel('Epoch')
ax.set_ylabel('Error')
ax.set_title('Error vs. Epoch')

# Eliminate margins
ax.margins(x=0)

# Show dashed grid lines
ax.grid(True)

# Adjust y-axis to eliminate margin below the origin
ax.set_ylim(bottom=0)

plt.show()
