"""
A few comments:

The Nyquist frequency is half the sampling rate.
You are working with regularly sampled data, so you want a digital filter, not an analog filter.
This means you should not use analog=True
in the call to butter, and you should use scipy.signal.freqz (not freqs) to generate the frequency response.
One goal of those short utility functions is to allow you to leave all your frequencies expressed in Hz.
You shouldn't have to convert to rad/sec. As long as you
express your frequencies with consistent units, the scaling in the utility functions takes care of the normalization for you.

"""
import numpy as np
from scipy.signal import butter, lfilter, freqz
import matplotlib.pyplot as plt

def butter_lowpass(cutoff, fs, order=5):
    nyq = 0.5 * fs
    normal_cutoff = cutoff / nyq
    b, a = butter(order, normal_cutoff, btype='low', analog=False)
    return b, a

def butter_lowpass_filter(data, cutoff, fs, order=5):
    b, a = butter_lowpass(cutoff, fs, order=order)
    y = lfilter(b, a, data)
    return y

if __name__ == "__main__":
    # Filter requirements.
    order = 6
    fs = 11025.0       # sample rate, Hz
    cutoff = 5000.0  # desired cutoff frequency of the filter, Hz

    # Get the filter coefficients so we can check its frequency response.
    b, a = butter_lowpass(cutoff, fs, order)

    # Plot the frequency response.
    w, h = freqz(b, a, worN=8000)
    plt.subplot(2, 1, 1)
    plt.plot(0.5*fs*w/np.pi, np.abs(h), 'b')
    plt.plot(cutoff, 0.5*np.sqrt(2), 'ko')
    plt.axvline(cutoff, color='k')
    plt.xlim(0, 0.5*fs)
    plt.title("Lowpass Filter Frequency Response")
    plt.xlabel('Frequency [Hz]')
    plt.grid()
    plt.show()

