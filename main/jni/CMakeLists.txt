cmake_minimum_required(VERSION 3.4.1)
add_library(
    exploit_lib           # Nombre de la librería
    SHARED
    exploit_semclipboard.cpp  # Archivos fuente
    exploit_ioctl.cpp
    exploit_decon.cpp
)
find_library(log-lib log) # Enlaza la librería de logging de Android
target_link_libraries(exploit_lib
    android
    log
    c)