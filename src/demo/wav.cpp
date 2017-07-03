#include <stdio.h>

#include "wav.h"

namespace sc
{
namespace wav
{

bool WavFile::Open(const std::string& deviceName)
{
  m_fileFp = fopen(deviceName.c_str(), "wb");

  if (m_fileFp != NULL)
  {
    // Write RIFF chunk descriptor
    const char* riff = "RIFF";
    fwrite(riff, 4, 1, m_fileFp);
    // Placeholder for the ChunkSize
    const char dummy[4] = { 0 };
    fwrite(dummy, 4, 1, m_fileFp);
    // WAVE format
    const char* format = "WAVE";
    fwrite(format, 4, 1, m_fileFp);

    // Subchunk 1
    const char* fmt = "fmt ";
    fwrite(fmt, 4, 1, m_fileFp);
    uint32_t subchunk1Size = 16;
    fwrite(&subchunk1Size, 4, 1, m_fileFp);
    uint16_t audioFormat = 1;
    fwrite(&audioFormat, 2, 1, m_fileFp);
    uint16_t numChannels = 2;
    fwrite(&numChannels, 2, 1, m_fileFp);
    uint32_t sampleRate = 44100;
    fwrite(&sampleRate, 4, 1, m_fileFp);
    uint32_t byteRate = sampleRate * numChannels * 2;
    fwrite(&byteRate, 4, 1, m_fileFp);
    uint16_t blockAlign = 4;
    fwrite(&blockAlign, 2, 1, m_fileFp);
    uint16_t bitsPerSample = 16;
    fwrite(&bitsPerSample, 2, 1, m_fileFp);

    // Subchunk 2
    const char* data = "data";
    fwrite(data, 4, 1, m_fileFp);
    fwrite(dummy, 4, 1, m_fileFp);
  }

  return m_fileFp == NULL;
}

void WavFile::Write(const void* buffer, uint32_t size)
{
  fwrite(buffer, size, 1, m_fileFp);
  m_dataSize += size;
}

void WavFile::Close()
{
  fseek(m_fileFp, 4, SEEK_SET);
  uint32_t chunkSize = 36 + m_dataSize;
  fwrite(&chunkSize, 4, 1, m_fileFp);
  fseek(m_fileFp, 40, SEEK_SET);
  uint32_t subchunk2Size = m_dataSize;
  fwrite(&subchunk2Size, 4, 1, m_fileFp);
  fclose(m_fileFp);
}

}
}
