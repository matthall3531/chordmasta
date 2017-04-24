import numpy as np
import matplotlib.pyplot as plt
import scipy.fftpack
import wave
import sys

wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Am highest.wav")
#wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Gm highest.wav")
print("Sampling freq={0}, noOfChannels={1}".format(wavfile.getframerate(), wavfile.getnchannels()))

# Plot wav file
signal = wavfile.readframes(-1)
signal = np.fromstring(signal, 'Int16')
#Split the data into channels
channels = [[] for channel in range(wavfile.getnchannels())]
for index, datum in enumerate(signal):
    channels[index%len(channels)].append(datum)
#Get time from indices
fs = wavfile.getframerate()
Time=np.linspace(0, len(signal)/len(channels)/fs, num=len(signal)/len(channels))
#Plot
plt.figure(1)
plt.title('Signal Wave...')
for channel in channels:
    plt.plot(Time,channel)
plt.show()

# FFT test signal
print(len(channels[0]))
N = 2048
T = 1.0 / 44100.0
#x = np.linspace(0.0, N*T, N)
#print(x)
#y = np.sin(50.0 * 2.0*np.pi*x) + 0.5*np.sin(5000.0 * 2.0*np.pi*x)
ffts_rounds = len(channels[0])//N
for i in range(0, ffts_rounds):
    y = channels[0][i*N:i*N+N]
    # Number of samplepoints
    # sample spacing
    yf = scipy.fftpack.fft(y)
    xf = np.linspace(0.0, 1.0/(2.0*T), N/2)
    fig, ax = plt.subplots()
    ax.plot(xf, 2.0/N * np.abs(yf[:N//2]))
    plt.show()
