import sys
import math


__notes = []
__octave_note_names = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B', 'C']


def init():
    """Initialize the list with the 102-keys of the extended piano."""
    if len(__notes) > 0:
        print("__notes is already populated.")
        sys.exit(-1)
    for n in range(1, 102):
        freqLower = math.pow(2, (n - 50)/12) * 440
        freq = math.pow(2, (n - 49)/12) * 440
        freqUpper = math.pow(2, (n - 48)/12) * 440
        note_name = __octave_note_names[(n-1)%len(__octave_note_names)]
        key = {'frequency': freq,
               'lower': freqLower + (freq - freqLower)/2,
               'upper': freqUpper - (freqUpper - freq)/2,
               'note': note_name}
        __notes.append(key)
    print(__notes)


def binary_search(list_of_notes, freq):
    if len(list_of_notes) <= 1:
        return list_of_notes[0]
    middle_note = list_of_notes[len(list_of_notes)//2]
    if middle_note['lower'] <= freq < middle_note['upper']:
        return middle_note
    if freq < middle_note['lower']:
        return binary_search(list_of_notes[:len(list_of_notes)//2], freq)
    return binary_search(list_of_notes[len(list_of_notes)//2:], freq)


def identify_note(freq):
    return binary_search(__notes, freq)


if __name__ == "__main__":
    init()
    note = identify_note(440)
    print(note)
    note = identify_note(445)
    print(note)
    note = identify_note(28)
    print(note)
    note = identify_note(9000)
    print(note)