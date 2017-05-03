def create_mono_stream_float(stereo_data):
    """Stereo data is a 2-channel sample array where samples are interleaved"""
    channel0 = stereo_data[1::2]
    channel1 = stereo_data[2::2]
    c0len = len(channel0)
    c1len = len(channel1)
    channel0 = channel0[:min(c0len, c1len)]
    channel1 = channel1[:min(c0len, c1len)]

    return (channel0 + channel1)/2
