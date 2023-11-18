import os
import matplotlib.pyplot as plt

file_names = ['epsilon_0.0', 'original']
plot_names = ['On-policy Learning', 'Off-policy Learning']
title_name = '2(b) On-policy vs Off-policy'
save_name = title_name

def read_data(file_name):
    round_numbers = []
    total_rewards = []
    with open(os.path.join('..', 'log', file_name), 'r') as file:
        next(file)  # Skip header
        for line in file:
            round_number, _, total_reward = line.split(',')
            round_numbers.append(int(round_number))
            total_rewards.append(float(total_reward))
    return round_numbers, total_rewards

for file_name, plot_name in zip(file_names, plot_names):
    round_numbers, total_rewards = read_data(file_name)
    plt.plot(round_numbers, total_rewards, marker='o', label=plot_name)

plt.xlabel('Round Number')
plt.ylabel('Total Reward')
plt.title(title_name)
plt.grid()
plt.legend()
plt.savefig(os.path.join('fig', save_name))
