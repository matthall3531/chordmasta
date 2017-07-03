#ifndef _SINK_H_
#define _SINK_H_

#include <stdint.h>
#include <string>

namespace sc
{
namespace audio
{
class Sink
{
public:
  virtual bool Open(const std::string& deviceName) = 0;
  virtual void Write(const void* buffer, uint32_t bytes) = 0;
  virtual void Close() = 0;
};
}
}

#endif // _SINK_H_
