def identify_note(freq):
    """Find freq in the note list, should be within note boundaries.
       Use binary search."""
    note = None
    return note


def find_harmonics(frequency):
    """From a list of frequencies, this function finds the harmonics"""
    harmonics = []
    for freq in frequency:
        note = identify_note(freq)
        if note not in harmonics and note is not None:
            harmonics.append(note)
    return harmonics


