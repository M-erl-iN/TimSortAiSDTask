import random


file = open("massives.txt", "w")
for i in range(50, 10000, 50):
    file.write(str(i) + " " + " ".join([str(random.randint(1, 10000)) for _ in range(i)]) + "\n")
