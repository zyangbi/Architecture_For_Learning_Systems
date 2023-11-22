import os
import matplotlib.pyplot as plt

file_names = ['reward_0.0', 'original']
plot_names = ['Terminal Rewards', 'Intermediate Rewards']
title_name = '2(c) Terminal Rewards vs Intermediate Rewards'
save_name = title_name

def read_data(file_name):
    round_numbers = []
    wins = []
    with open(os.path.join('..', 'log', file_name), 'r') as file:
        next(file)  # Skip header
        for line in file:
            round_number, win, _ = line.split(',')
            round_numbers.append(int(round_number))
            wins.append(float(win))
    return round_numbers, wins

for file_name, plot_name in zip(file_names, plot_names):
    round_numbers, total_rewards = read_data(file_name)
    plt.plot(round_numbers, total_rewards, marker='o', label=plot_name)

plt.xlabel('Round Number')
plt.ylabel('Wins in 100 Rounds')
plt.title(title_name)
plt.grid()
plt.legend()
plt.savefig(os.path.join('fig', save_name))
