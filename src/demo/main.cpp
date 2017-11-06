#include <stdio.h>

#include "portaudio.h"

#ifdef WIN32
#include <Windows.h>
#endif

#include "wav.h"

using namespace sc;

#ifdef __linux__
  const PaHostApiTypeId hostTypeId = PaHostApiTypeId::paALSA;
#else
  const PaHostApiTypeId hostTypeId = PaHostApiTypeId::paMME;
#endif

int streamCallback(const void *input,
  void *output,
  unsigned long frameCount,
  const PaStreamCallbackTimeInfo* timeInfo,
  PaStreamCallbackFlags statusFlags,
  void *userData)
{
  printf("streamCallback() - frameCount=%d, time=%f, flags=%u\n",
    frameCount,
    timeInfo->inputBufferAdcTime,
    statusFlags);
  wav::WavFile* wavFile = static_cast<wav::WavFile*>(userData);
  wavFile->Write(input, frameCount);
  return paContinue;
}

int main(int argc, char** argv)
{
  PaError status = Pa_Initialize();
  if (status != paNoError)
  {
    printf("Error while initializing PortAudio - aborting\n");
  }

  int portAudioVersion = Pa_GetVersion();
  printf("Port Audio version is %08x\n", portAudioVersion);

  PaHostApiIndex index = Pa_HostApiTypeIdToHostApiIndex(hostTypeId);
  if (index < 0)
  {
    printf("Host API type index %d is not available.\n", hostTypeId);
  }
  else
  {
    const PaHostApiInfo* hostApiInfo = Pa_GetHostApiInfo(index);
    const PaDeviceIndex deviceIndex = hostApiInfo->defaultInputDevice;

    const PaDeviceInfo* deviceInfo = Pa_GetDeviceInfo(deviceIndex);

    printf("Device info : %s (api=%s)\n",
      deviceInfo->name,
      hostApiInfo->name);

    PaStreamParameters streamParams;
    streamParams.device = deviceIndex;
    streamParams.channelCount = 2;
    streamParams.sampleFormat = paInt16;
    streamParams.suggestedLatency = 0.100;
    streamParams.hostApiSpecificStreamInfo = NULL;

    if (Pa_IsFormatSupported(&streamParams, NULL, 44100.0) < 0)
    {
      printf("Format is not supported.\n");
    }
    else
    {
      wav::WavFile wavFile;

      if (!wavFile.Open("mic.wav"))
      {
        printf("Unable to open wavfile!\n");
      }

      PaStream* stream;
      if (Pa_OpenStream(&stream, &streamParams, NULL, 44100.0, 1024, 0, streamCallback, &wavFile) < 0)
      {
        printf("Failed open stream.\n");
      }
      else
      {
        Pa_StartStream(stream);
        Sleep(5000);
        wavFile.Close();
      }
    }
    printf("Done\n");
  }

  Pa_Terminate();

  return 0;
}
