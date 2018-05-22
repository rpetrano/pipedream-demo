FROM centos:7

ARG BUILD_UID
ARG BUILD_GID
RUN groupadd -g $BUILD_GID build && useradd -u $BUILD_UID -g $BUILD_GID -s /bin/bash -m build

RUN echo "project 1!"
RUN sleep 10s
