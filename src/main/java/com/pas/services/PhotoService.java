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
        photos.add(new Photo("images/on-bikes.jpg", "images/on-bikes.jpg",
                "On Bikes", "On Bikes"));
        photos.add(new Photo("images/pointing.jpg", "images/pointing.jpg",
                "Pointing", "Pointing"));
        photos.add(new Photo("images/taxi.jpg", "images/taxi.jpg",
                "Taxi", "Taxi"));
        photos.add(new Photo("images/thumbs-up.jpg", "images/thumbs-up.jpg",
                "Thumbs Up", "Thumbs Up"));
        photos.add(new Photo("images/which-way.jpg", "images/which-way.jpg",
                "Which Way", "Which Way"));
        photos.add(new Photo("images/writing.jpg", "images/writing.jpg",
                "Writing", "Writing"));
       
    }

    public List<Photo> getPhotos() 
    {
        return photos;
    }
}