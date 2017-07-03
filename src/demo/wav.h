#ifndef _WAV_H_
#define _WAV_H_

#include "sink.h"

#include <string>

namespace sc
{
namespace wav
{ 

class WavFile : public audio::Sink
{
public:
  WavFile() : m_dataSize(0) {}
//  virtual ~WavFile();

  virtual bool Open(const std::string& deviceName);
  virtual void Write(const void* buffer, uint32_t bytes);
  virtual void Close();

private:
  FILE* m_fileFp;
  uint32_t m_dataSize;
};

}
}


#endif // _WAV_H_
