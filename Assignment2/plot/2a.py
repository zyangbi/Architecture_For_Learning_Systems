import os
import matplotlib.pyplot as plt


file_name = 'original'
title = '2(a) Total Reward vs Round Number'
save_name = title

round_numbers = []
total_rewards = []
with open(os.path.join('..', 'log', file_name), 'r') as file:
    next(file)
    for line in file:
        round_number, _, total_reward = line.split(',')
        round_numbers.append(int(round_number))
        total_rewards.append(float(total_reward))

plt.plot(round_numbers, total_rewards, marker='o')
plt.xlabel('Round Number')
plt.ylabel('Total Reward')
plt.title(title)
plt.grid()
plt.savefig(os.path.join('fig', save_name))
