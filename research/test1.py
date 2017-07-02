import numpy as np
import matplotlib.pyplot as plt
import scipy.fftpack
import wave

import util
import filter
import note
import harmonics as harm

notetable = note.create_note_table()

#wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Am highest.wav")
#wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Gm highest.wav")
wavfile = wave.open("./testfiles/440Hz_44100Hz_16bit_05sec.wav")

fs = wavfile.getframerate()
nchannels = wavfile.getnchannels()

print("Sampling freq={0}, noOfChannels={1}".format(fs, nchannels))

# Plot wav file
signal = wavfile.readframes(-1)
signal = np.fromstring(signal, 'Int16')

if nchannels > 1:
    signal = util.create_mono_stream_float(signal)

# Get time from indices
#fs = wavfile.getframerate()
#Time=np.linspace(0, len(mono)/fs, len(mono))
# Plot
#plt.figure(1)
#plt.title('Signal Wave...')
#plt.plot(Time, mono)
# plt.show()

# Re-sample signal at 11025 Hz (Maxing out at 5512.5 Hz)
signal = signal[::4]
fs = 11025
signal = filter.butter_lowpass_filter(signal, 5000, fs, order=5)

# FFT test signal
N = fs #fs//2
T = 1.0 / fs
#x = np.linspace(0.0, N*T, N)
#print(x)
#y = np.sin(50.0 * 2.0*np.pi*x) + 0.5*np.sin(5000.0 * 2.0*np.pi*x)
# ffts_rounds = len(mono)//N
#for i in range(0, ffts_rounds):
#    y = channels[0][i*N:i*N+N]
    # Number of samplepoints
    # sample spacing
yf = scipy.fftpack.fft(signal[:N])
xf = np.linspace(0.0, 1.0/(2.0*T), N//2)
fig, ax = plt.subplots()
ax.plot(xf, 2.0/N * np.abs(yf[0:N//2]))
plt.show()

# Use the first half of the data and convert to magnitude
X = np.abs(yf[0:len(yf)//2])
#print(type(X))

# Normalize X
X = X/sum(X)

# Save the 20 last and reverse
sorted1 = X.argsort()[-100:][::-1]

harm.init()
harmonics = harm.find_harmonics(sorted1, 3)
print(harmonics)

#area = sum(X[sorted1])
#print("Area of the 100 first : {}".format(area))

#for i in range(0, len(sorted1)):
#    print("Frequency: {0} Area: {1}".format(sorted1[i], X[sorted1[i]]))

#print(sorted1*(fs/N))
