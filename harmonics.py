import piano_notes as notes


def find_harmonics(frequency, number_of_harmonics = 3):
    """From a list of frequencies, this function finds the harmonics
    The frequency list is the result of a signal analysis.
    Harmonics should be the number_of_harmonics_result harmonics in the frequency list."""
    harmonics = []
    for freq in frequency:
        note = notes.identify_note(freq)
        if note not in harmonics and note is not None:
            harmonics.append(note)
            if len(harmonics) >= number_of_harmonics:
                break
    return harmonics


def init():
    notes.init()
