import os
import matplotlib.pyplot as plt

logFileName = 'epsilon_0.0'
title = '2(b) On-policy Learning'
logFileName = 'original'
title = '2(a)  round=5000'

round_numbers = []
total_rewards = []
# with open(os.path.join('..', 'out', 'production', 'Assignment2', 'MicroBot.data', logFileName), 'r') as file:
with open(os.path.join('..', 'log', logFileName), 'r') as file:
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
plt.savefig(logFileName + '.png')
