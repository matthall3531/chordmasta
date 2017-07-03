cmake_minimum_required(VERSION 2.8)
project(demo)

set(SRC
    main.cpp
	wav.cpp)

set(LIBS
    portaudio_static_x86.lib)

link_directories(${CMAKE_SOURCE_DIR}/../lib)

include_directories(${CMAKE_SOURCE_DIR}/../external/portaudio/include)

add_executable(chordmasta_demo 
               ${SRC})

target_link_libraries(chordmasta_demo 
                      ${LIBS})
