import numpy as np
import matplotlib.pyplot as plt
import scipy.fftpack
import wave
import util
import filter

wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Am highest.wav")
# wavfile = wave.open("./testfiles/Grand Piano - Fazioli - minor chords - Gm highest.wav")
print("Sampling freq={0}, noOfChannels={1}".format(wavfile.getframerate(), wavfile.getnchannels()))

# Plot wav file
signal = wavfile.readframes(-1)
signal = np.fromstring(signal, 'Int16')
mono = util.create_mono_stream_float(signal)

# Get time from indices
fs = wavfile.getframerate()
Time=np.linspace(0, len(mono)/fs, len(mono))
# Plot
plt.figure(1)
plt.title('Signal Wave...')
plt.plot(Time, mono)
# plt.show()

# Re-sample signal at 11025 Hz (Maxing out at 5512.5 Hz)
mono = mono[::4]
fs = 11025
mono = filter.butter_lowpass_filter(mono, 5000, fs, order=5)

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
yf = scipy.fftpack.fft(mono[:N])
xf = np.linspace(0.0, 1.0/(2.0*T), N//2)
fig, ax = plt.subplots()
ax.plot(xf, 2.0/N * np.abs(yf[0:N//2]))
#plt.show()

# Use the first half of the data and convert to magnitude
X = np.abs(yf[0:len(yf)//2])
print("Done")