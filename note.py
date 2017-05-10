import math


def create_note_table():
    notes = []
    for i in range(1, 88):
        f = 440 * math.pow(2, ((float(i)-49.0)/12.0))
        d = {"name": "Key " + str(i), "frequency": f}
        notes.append(d)
    return notes
