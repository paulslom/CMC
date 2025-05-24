package com.pas.services;

import java.util.ArrayList;
import java.util.List;

import com.pas.pojo.Photo;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class PhotoService 
{
    private List<Photo> photos;

    @PostConstruct
    public void init() 
    {
        photos = new ArrayList<>();

        photos.add(new Photo("images/being-driven.jpg", "images/being-driven.jpg",
                "Driven", "Driven"));
        photos.add(new Photo("images/bikes.jpeg", "images/bikes.jpeg",
                "Bikes", "Bikes"));
        photos.add(new Photo("images/pointing.jpg", "images/pointing.jpg",
                "Pointing", "Pointing"));
        photos.add(new Photo("images/taxi.jpeg", "images/taxi.jpeg",
                "Taxi", "Taxi"));
        photos.add(new Photo("images/thumbs-up.jpeg", "images/thumbs-up.jpeg",
                "Thumbs Up", "Thumbs Up"));
        photos.add(new Photo("images/riding.jpeg", "images/riding.jpeg",
                "Riding", "Riding"));
        photos.add(new Photo("images/riding-2.jpeg", "images/riding-2.jpeg",
                "Riding2", "Riding2"));
        photos.add(new Photo("images/train.jpg", "images/train.jpg",
                "Train", "Train"));
       
    }

    public List<Photo> getPhotos() 
    {
        return photos;
    }
}